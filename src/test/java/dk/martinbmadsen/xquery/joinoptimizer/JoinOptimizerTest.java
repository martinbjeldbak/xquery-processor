package dk.martinbmadsen.xquery.joinoptimizer;

import dk.martinbmadsen.utils.debug.SampleReader;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
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
}
