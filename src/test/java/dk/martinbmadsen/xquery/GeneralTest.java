package dk.martinbmadsen.xquery;

import dk.martinbmadsen.utils.debug.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.junit.Test;

import java.util.List;

public class GeneralTest extends XQueryTest {

    @Test
    public void playground() {
        List<IXMLElement> res = ex("for $a in doc(\"samples/xml/j_caesar.xml\")//ACT\n" +
                "  return for $b in doc(\"samples/xml/j_caesar.xml\")/*\n" +
                "  return <h>{$b}</h>");
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
        assertXMLEquals("<a>h<b>ello</b></a>", res, 0);
    }

    @Test
    public void stringConstantOnly() {
        String t = "this is a string constant ";

        List<IXMLElement> res = ex("\"" + t + "\"");
        assertXMLEquals(t, res, 0);
    }
}
