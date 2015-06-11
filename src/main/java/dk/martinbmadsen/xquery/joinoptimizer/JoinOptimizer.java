package dk.martinbmadsen.xquery.joinoptimizer;

import java.util.HashMap;
import java.util.Map;

public class JoinOptimizer {
    private Map<String, String> forVarMap;

    // TODO: Need to figure out where the for loop(s) are. For now I just assume we are given a "for" that possibly needs to be optimized
    // TODO: So far, this does not support complicated assignments, only $b in doc("input")/book since we are splitting on whitespace
    public JoinOptimizer(String query) {
        forVarMap = getForAssignment(query);

    }

    private HashMap<String, String> getForAssignment(String query) {
        String[] words = query.replace(',', ' ').split("\\s+");
        HashMap<String, String> nodes = new HashMap<>();
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