package dk.martinbmadsen.xquery;

import dk.martinbmadsen.utils.debug.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GeneralTest extends XQueryTest {

    @Test
    public void playground() {
        List<IXMLElement> res = ex("let $v := \"h\", $v2 := \"ello\" <a>{$v, <b>{$v2}</b>}</a>");
        XQueryExecutor.printResults(res);
    }

    @Test
    public void letTest1() {
        List<IXMLElement> res = ex("let $v := \"h\" <hello>{$v}</hello>");

        assertXMLEquals("<hello>h</hello>", res, 0);
    }

    @Test
    public void letTest2() {
        List<IXMLElement> res = ex("let $v := \"h\", $v2 := \"ello\" <a>{$v, <b>{$v2}</b>}</a>");

        // TODO: Run this test and note that we don't properly nest XML elements. I haven't been able to
        // figure out how yet. XqEvaluator.evalTagname() makes new XMLElements, we could loop through the
        // result list from xq and make each a child element of the first element of the list, or just a long
        // list of child elements, where each item in the list is a child of the previous item. Will need to
        // play around with this.
        assertXMLEquals("<a>h&lt;b&gt;ello&lt;/b&gt;</a>", res, 0);
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
