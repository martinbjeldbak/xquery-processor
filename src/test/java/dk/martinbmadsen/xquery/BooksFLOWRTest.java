package dk.martinbmadsen.xquery;

import dk.martinbmadsen.utils.debug.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.junit.Test;

import java.util.List;

public class BooksFLOWRTest extends XQueryTest {
    // books.xml from http://www.w3schools.com/xquery/xquery_flwor.asp

    @Test
    public void playground() {
        String query = "<ul>\n" +
                "{\n" +
                "for $x in doc(\"books.xml\")/bookstore/book/title\n" +
                "order by $x\n" +
                "return <li>{$x}</li>\n" +
                "}\n" +
                "</ul>";
    }

    @Test
    public void flowrTest() {
        String query =
                "for $x in doc(\"samples/xml/books.xml\")/bookstore/book\n" +
                        "where $x/year=\"2003\"\n" +
                        "return $x/title";

        List<IXMLElement> res = ex(query);

        XQueryExecutor.printResults(res);

        //assertXMLEquals("<title lang=\"en\">Harry Potter</title>", res, 0);
        //assertXMLEquals("<title lang=\"en\">XQuery Kick Start</title>", res, 1);
    }
}
