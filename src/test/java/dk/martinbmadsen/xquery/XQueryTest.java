package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.executor.XQueryExecutor;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLElement;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XQueryTest {
    protected String r;
    private XPathFactory xpfac =  XPathFactory.instance();

    protected List<IXMLElement> runCorrectImplementation(String filePath, String query) {
        XPathExpression<Element> xp = xpfac.compile(query, Filters.element());
        Document root = null;

        try {
            root = new SAXBuilder().build(filePath);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

        XQueryListValue res = new XQueryListValue();

        for(Element e : xp.evaluate(root)) {
            res.add(new XMLElement(e));
        }

        return res;
    }

    protected List<IXMLElement> ex(String q) {
        return XQueryExecutor.executeFromString(q);
    }

    protected List<IXMLElement> exF(String f) throws IOException {
        return XQueryExecutor.executeFromFile(f);
    }

    protected void assertXMLEquals(String expected, List<IXMLElement> actual, int actualIndex) {
        assertEquals(expected, actual.get(actualIndex).toString());
    }

    protected void print(List<IXMLElement> res) {
        XQueryExecutor.printResults(res);
    }
}
