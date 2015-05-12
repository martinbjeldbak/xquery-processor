package dk.martinbmadsen.xquery;

import dk.martinbmadsen.utils.debug.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BooksFLOWRTest extends XQueryTest {
    // books.xml from http://www.w3schools.com/xquery/xquery_flwor.asp

    @Test
    public void playground() {
        String q = "for $x in (doc(\"samples/xml/books.xml\")//author)\n" +
                "where $x/text() = \"J K. Rowling\"\n" +
                " return $x/text()";

        List<IXMLElement> res = ex(q);

        XQueryExecutor.printResults(res);
        /*
        String query = "<ul>\n" +
                "{\n" +
                "for $x in doc(\"books.xml\")/bookstore/book/title\n" +
                "return <li>{$x}</li>\n" +
                "}\n" +
                "</ul>";
                */
    }

    @Test
    public void authorTest1() {
        String q = "for $x in doc(\"samples/xml/books.xml\")//author" +
                "  return $x/text()";

        List<IXMLElement> res = ex(q);

        assertEquals(8, res.size());
        assertXMLEquals("Giada De Laurentiis", res, 0);
        assertXMLEquals("Erik T. Ray", res, 7);
    }

    @Test
    public void authorWhereTest1() {
        String q = "for $x in doc(\"samples/xml/books.xml\")//author\n" +
                "\twhere $x/text() = \"J K. Rowling\"\n" +
                " return $x/year/text()";

        List<IXMLElement> res = ex(q);

        XQueryExecutor.printResults(res);

        assertEquals(1, res.size());
        assertXMLEquals("2005", res, 0);
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
