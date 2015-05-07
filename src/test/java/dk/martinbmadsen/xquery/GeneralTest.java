package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GeneralTest extends XQueryTest {
    @Test
    public void stringConstantOnly() {
        String t = "this is a string constant ";
        List<IXMLElement> res = ex("\"" + t + "\"");
        assertEquals(t, res.get(0).toString());
    }

    @Test
    public void variableOnly() {
        List<IXMLElement> res = ex("$lol");
        assertEquals("lol ", res.get(0).toString());
    }
}
