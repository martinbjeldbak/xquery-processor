package dk.martinbmadsen.xquery.joinoptimizer;

import org.jgrapht.Graph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.StringEdgeNameProvider;
import org.jgrapht.ext.VertexNameProvider;

import java.io.*;

public class GraphPrinter {
    public static class XQueryVertexNameProvider<T> implements VertexNameProvider<T> {
        @Override
        public String getVertexName(T vertex) {
            return vertex.toString().replace("$", "").replaceAll("\"", "").replaceAll("\\(", "_").replaceAll("\\)", "_");
        }
    }

    public static void printGraph(Graph graph, String targetDirectory, String fileName) {
        VertexNameProvider<String> stringNameProvider = new XQueryVertexNameProvider<>();
        EdgeNameProvider<String> stringEdgeNameProvider = new StringEdgeNameProvider<>();

        DOTExporter exporter = new DOTExporter<>(stringNameProvider,
                stringNameProvider, stringEdgeNameProvider);
        new File(targetDirectory).mkdirs();

        String fullPath = targetDirectory + "/" + fileName;

        try {
            exporter.export(new FileWriter(fullPath + ".dot"), graph);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Runtime r = Runtime.getRuntime();
        try {
            Process p = r.exec(String.format("/usr/local/bin/dot -Tpng %s -o %s", fullPath + ".dot", fullPath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
