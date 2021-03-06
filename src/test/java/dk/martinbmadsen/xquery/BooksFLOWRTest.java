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
        //XQueryExecutor.printPrettyResults(res);
    }

    @Test
    public void exampleJKTests() {
        String q1 = "for $x in doc(\"samples/xml/books.xml\")/book\n" +
                "\tlet $y := $x/author, $year := \"2005\", $jk := \"J K. Rowling\"\n" +
                " where $y/text() = $jk and $x/year/text() = $year\n" +
                " return $x/price/text()";

        String q2 = "for $x in doc(\"samples/xml/books.xml\")/book\n" +
                "\tlet $y := \"J K. Rowling\", $year := \"2005\"\n" +
                " where $x/author/text() = $y and $x/year/text() = $year\n" +
                " return $x/price/text()";

        String q3 = "for $x in doc(\"samples/xml/books.xml\")/book\n" +
                "\tlet $y := \"J K. Rowling\"\n" +
                " where $x/author/text() = $y\n" +
                " return $x/price/text()";

        assertEquals(ex(q1), ex(q2));
        assertEquals(ex(q2), ex(q3));
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
        String q = "for $x in doc(\"samples/xml/books.xml\")/book\n" +
                "where $x/author/text() = \"J K. Rowling\"\n" +
                " return $x/year/text()";

        List<IXMLElement> res = ex(q);

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

        XQueryExecutor.printPrettyResults(res);

        //assertXMLEquals("<title lang=\"en\">Harry Potter</title>", res, 0);
        //assertXMLEquals("<title lang=\"en\">XQuery Kick Start</title>", res, 1);
    }

    @Test
    public void listCreationTest() {
        String query = "<ul>\n" +
                "{\n" +
                "for $x in doc(\"samples/xml/books.xml\")/book/title\n" +
                "return <li>{$x}</li>\n" +
                "}\n" +
                "</ul>";
        List<IXMLElement> res = ex(query);

        assertEquals(1, res.size());
    }
}
