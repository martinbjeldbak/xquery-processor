package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.xquery.executor.XQueryExecutor;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XQueryTest {
    protected String r;

    protected List<IXMLElement> ex(String q) {
        return XQueryExecutor.executeFromString(q);
    }

    protected List<IXMLElement> exF(String f) throws IOException {
        return XQueryExecutor.executeFromFile(f);
    }

    protected void assertXMLEquals(String expected, List<IXMLElement> actual, int actualIndex) {
        assertEquals(expected, actual.get(actualIndex).toString());
    }
}
