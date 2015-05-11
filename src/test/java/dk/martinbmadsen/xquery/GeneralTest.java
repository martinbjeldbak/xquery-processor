package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.executor.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GeneralTest extends XQueryTest {

    @Test
    public void playground() {
        List<IXMLElement> res = ex("let $v := \"h\" <hello>{$v}</hello>");

        XQueryExecutor.printResults(res);
    }

    @Test
    public void letTest1() {
        List<IXMLElement> res = ex("let $v := \"h\" <hello>{$v}</hello>");

        assertXMLEquals("<hello>h</hello>", res, 0);
    }

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
