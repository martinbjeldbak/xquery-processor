package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class ShakespeareTest extends XQueryTest {
    private String r = "doc(\"samples/xml/j_caesar.xml\")/"; // root query

    @Test
    public void canExecuteFromFile() throws IOException {
        List<IXMLElement> result = exR("TITLE");

        assertEquals("<TITLE>The Tragedy of Julius Caesar</TITLE>", result.get(0).toString());
    }

    @Test
    public void loads9Personae() {
        List<IXMLElement> res = exR("PERSONAE/PERSONA");

        assertEquals(9, res.size());
    }

    private List<IXMLElement> exF() throws IOException {
        return super.exF("samples/xquery/test.xq");
    }

    private List<IXMLElement> exR(String q) {
        return super.ex(r + q);
    }
}
