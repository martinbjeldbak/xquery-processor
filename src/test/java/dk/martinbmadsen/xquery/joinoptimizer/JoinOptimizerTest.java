package dk.martinbmadsen.xquery.joinoptimizer;

import dk.martinbmadsen.utils.debug.SampleReader;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JoinOptimizerTest {
    @Test
    public void playground() throws IOException {
        String query = SampleReader.openQueryFileAsString("joinNotesEx73.xq");
        JoinOptimizer jo = new JoinOptimizer(query, "joinNotesEx73");
        jo.graphToPNG();
    }

    @Test
    public void joinNotesEx72ForMapping() throws IOException {
        String query = SampleReader.openQueryFileAsString("joinNotesEx72.xq");
        JoinOptimizer jo = new JoinOptimizer(query, "joinNotesEx72");

        Map<String, String> expected = new HashMap<>(4);
        expected.put("$b",  "doc(\"input\")/book");
        expected.put("$a",  "doc(\"input\")/entry");
        expected.put("$tb", "$b/title");
        expected.put("$ta", "$a/title");

        assertEquals(expected, jo.getForVarMap());
    }

    @Test
    public void joinNotesEx72EqMapping() throws IOException {
        String query = SampleReader.openQueryFileAsString("joinNotesEx72.xq");
        JoinOptimizer jo = new JoinOptimizer(query, "joinNotesEx72");

        Map<String, List<List<String>>> expected = new HashMap<>();
        List<List<String>> comparisons = new ArrayList<>(1);
        List<String> comparison1       = new ArrayList<>(2);
        comparison1.add("$tb");
        comparison1.add("$ta");

        comparisons.add(comparison1);

        expected.put("eq", comparisons);

        assertEquals(expected, jo.getComparisonMap());
    }

    /*
    @Test
    public void joinNotesEx73ForMapping() throws IOException {
        String query = SampleReader.openQueryFileAsString("joinNotesEx73.xq");
        JoinOptimizer jo = new JoinOptimizer(query, "joinNotesEx73");
        jo.graphToPNG();
    }
    */

    @Test
    public void milestone2Q1ForMapping() throws IOException {
        String query = SampleReader.openQueryFileAsString("Milestone2Q1");
        JoinOptimizer jo = new JoinOptimizer(query, "Milestone2Q1");

        Map<String, String> expected = new HashMap<>(4);
        expected.put("$s",  "document(\"samples/xml/j_caesar.xml\")//SPEAKER");
        expected.put("$a",  "document(\"samples/xml/j_caesar.xml\")//ACT");
        expected.put("$sp", "$a//SPEAKER");
        expected.put("$stxt", "$s/text()");

        assertEquals(expected, jo.getForVarMap());
    }

    @Test
    public void milestone2Q1EqMapping() throws IOException {
        String query = SampleReader.openQueryFileAsString("Milestone2Q1");
        JoinOptimizer jo = new JoinOptimizer(query, "Milestone2Q1");

        Map<String, List<List<String>>> expected = new HashMap<>();
        List<List<String>> comparisons = new ArrayList<>(2);
        List<String> comparison1       = new ArrayList<>(2);
        List<String> comparison2       = new ArrayList<>(2);
        comparison1.add("$sp");
        comparison1.add("$s");
        comparison2.add("$stxt");
        comparison2.add("CASCA");

        comparisons.add(comparison1);
        comparisons.add(comparison2);

        expected.put("eq", comparisons);

        assertEquals(expected, jo.getComparisonMap());
    }

    @Test
    public void milestone2Q3ForMapping() throws IOException {
        String query = SampleReader.openQueryFileAsString("Milestone2Q3");
        JoinOptimizer jo = new JoinOptimizer(query, "Milestone2Q3" );

        Map<String, String> expected = new HashMap<>(10);
        expected.put("$a", "document(\"samples/xml/j_caesar.xml\")//ACT");
        expected.put("$at", "$a/TITLE/text()");
        expected.put("$sc1", "$a//SCENE/TITLE");
        expected.put("$sc2", "$a//SCENE/TITLE");
        expected.put("$sc3", "$a//SCENE/TITLE");
        expected.put("$sc4", "$a//SCENE/TITLE");

        expected.put("$sp1", "$sc1/..//SPEAKER");
        expected.put("$sp2", "$sc2/..//SPEAKER");
        expected.put("$sp3", "$sc3/..//SPEAKER");
        expected.put("$sp4", "$sc4/..//SPEAKER");

        assertEquals(expected, jo.getForVarMap());
    }

    @Test
    public void milestone2QEqMapping() throws IOException {
        String query = SampleReader.openQueryFileAsString("Milestone2Q3");
        JoinOptimizer jo = new JoinOptimizer(query, "Milestone2Q3");

        Map<String, List<List<String>>> expected = new HashMap<>();
        List<List<String>> comparisons = new ArrayList<>(7);
        List<String> comparison1 = new ArrayList<>(2);
        List<String> comparison2 = new ArrayList<>(2);
        List<String> comparison3 = new ArrayList<>(2);
        List<String> comparison4 = new ArrayList<>(2);
        List<String> comparison5 = new ArrayList<>(2);
        List<String> comparison6 = new ArrayList<>(2);
        List<String> comparison7 = new ArrayList<>(2);
        comparison1.add("$at");
        comparison1.add("ACT I");
        comparison2.add("$sp1");
        comparison2.add("$sp2");
        comparison3.add("$sp2");
        comparison3.add("$sp3");
        comparison4.add("$sp3");
        comparison4.add("$sp4");
        comparison5.add("$sc1");
        comparison5.add("$sc2");
        comparison6.add("$sc2");
        comparison6.add("$sc3");
        comparison7.add("$sc3");
        comparison7.add("$sc4");

        comparisons.add(comparison1);
        comparisons.add(comparison2);
        comparisons.add(comparison3);
        comparisons.add(comparison4);
        comparisons.add(comparison5);
        comparisons.add(comparison6);
        comparisons.add(comparison7);

        expected.put("eq", comparisons);

        assertEquals(expected, jo.getComparisonMap());
    }
}
