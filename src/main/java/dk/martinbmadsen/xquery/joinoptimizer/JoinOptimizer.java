package dk.martinbmadsen.xquery.joinoptimizer;

import dk.martinbmadsen.utils.debug.Debugger;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinOptimizer {
    public Map<String, List<List<String>>> getComparisonMap() {
        return comparisonMap;
    }

    public static class PathEdge<V> extends DefaultEdge {
        private V v1;
        private V v2;
        private String label;

        public PathEdge(V v1, V v2, String label) {
            this.v1 = v1;
            this.v2 = v2;
            this.label = label;
        }

        public V getV1() {
            return v1;
        }

        public V getV2() {
            return v2;
        }

        public String toString() {
            return label;
        }
    }

    private String fileName;
    private Map<String, String> forVarMap;
    private Map<String, List<List<String>>> comparisonMap;
    private DirectedGraph dependencyGraph;

    // TODO: Need to figure out where the for loop(s) are. For now I just assume we are given a "for" that possibly needs to be optimized
    // TODO: So far, this does not support complicated assignments, only $b in doc("input")/book since we are splitting on whitespace
    public JoinOptimizer(String query, String fileName) {
        this.fileName = fileName;
        forVarMap = createForAssignmentMap(query);
        dependencyGraph = createDependencyGraph(forVarMap);
        comparisonMap = createComparisonMap(query);
    }

    private DirectedGraph createDependencyGraph(Map<String, String> forVarMap) {
        Pattern pattern = Pattern.compile("(((\\$\\w+)|(.+))/)?(.+)"); // match an optional variable ($words) followed by some text
        // Pattern is grouped into the following 5 groups:
        // 0: Entire string
        // 1: Root part of path, either variable or string
        //   2: Group 1 with '/' char chopped off
        //       3: Variable match
        //       4: Root (not variable match)
        // 5: Path

        DirectedGraph<String, PathEdge> graph = new DefaultDirectedGraph<>(PathEdge.class);

        for(Map.Entry<String, String> entry : forVarMap.entrySet()) {
            String key = entry.getKey();
            graph.addVertex(key);

            Matcher matcher = pattern.matcher(entry.getValue());

            if(matcher.find()) {
                String root = matcher.group(2);
                String path = matcher.group(5);

                graph.addVertex(root);
                graph.addEdge(key, root, new PathEdge<>(key, root, path));
            }

            /*
            System.out.println("Splitting " + entry.getValue());
            if(matcher.find()) {
                for(int i = 0; i <= matcher.groupCount(); i++) {
                    System.out.println("  group " + i + ": " + matcher.group(i));
                }
            }
            */
        }
        return graph;
    }

    public void graphToPNG() {
        GraphPrinter.printGraph(dependencyGraph, "samples/xquery/dependencies", fileName);
    }

    private Map<String, List<List<String>>> createComparisonMap(String query) {
        String[] words = query.replace(',', ' ').split("\\s+");
        Map<String, List<List<String>>> comparitorMap = new HashMap<>();

        int i;
        boolean inWhere = false;

        for(i = 0; i < words.length; i++) {
            String word = words[i];

            if(word.equals("where")) {
                inWhere = true;
                continue;
            }

            if(word.equals("return"))
                break;

            if(inWhere) { // Swallow comparison: v1 eq v2
                String l = word;
                String comparitor = comparitorConversion(words[++i]);
                String r = words[++i].replaceAll("\"", "");
                String extension = "";

                if(words[i+1].equals("and")) { // only 'and' is supported for now
                    extension = words[++i];
                }

                List<String> pair = new ArrayList<>(2);
                pair.add(l);
                pair.add(r);

                if(comparitorMap.get(comparitor) == null)
                    comparitorMap.put(comparitor, new ArrayList<>());

                comparitorMap.get(comparitor).add(pair);

                //System.out.println(String.format("%s %s %s %s", l, comparitor, r, extension));
            }
        }

        /*
        for(List<String> l : comparitorMap.get("eq")) {
            System.out.println(l.toString());
        }
        */
        return comparitorMap;
    }

    private String comparitorConversion(String comparitor) {
        String ret = "";
        switch (comparitor) {
            case "eq":
            case "=":
                ret = "eq";
                break;
            default:
                Debugger.error(String.format("Could not convert comparitor %s", comparitor));
                break;

        }
        return ret;
    }

    private Map<String, String> createForAssignmentMap(String query) {
        String[] words = query.replace(',', ' ').split("\\s+");
        Map<String, String> nodes = new HashMap<>();
        // loop through for part
        int i;
        boolean inFor = false;
        for(i = 0; i < words.length; i++) {
            String word = words[i];

            if(word.equals("for")) { // we are entering a "for statement"
                inFor = true;
                continue;
            }
            if(word.equals("let") || word.equals("where") || word.equals("return")) // we are exiting a "for"
                break;

            if(inFor) {
                // Swallow assignment line $var in $expression
                String variable = word;
                String in = words[++i]; // loop past "in" keyword
                String value = words[++i];
                // System.out.println("putting var '" + variable + "' to value '" + value + "'");
                nodes.put(variable, value);
            }
        }
        return nodes;
    }

    public Map<String, String> getForVarMap() {
        return forVarMap;
    }
}