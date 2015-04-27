package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.executor.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLElement;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Text;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LemonadeTest extends XQueryTest {
    private String r = "doc(\"samples/xml/lemonade2.xml\")/"; // root query

    @Test
    public void playground() {
        List<IXMLElement> res = exR("Fasdf");
        XQueryExecutor.printResults(res);
    }

    @Test
    public void lemonadePriceQuery() {
        List<IXMLElement> res = exR("drink/lemonade/price");

        XQueryExecutor.printResults(res);

        assertEquals(2, res.size());
        assertEquals("<price>$2.50</price>", res.get(0).toString());
        assertEquals("<price>$3.50</price>", res.get(1).toString());
    }

    @Test
    public void lemonadeAmountQuery() {
        List<IXMLElement> res = exR("drink/lemonade/amount");

        assertEquals(2, res.size());
        assertEquals("<amount>20</amount>", res.get(0).toString());
        assertEquals("<amount>10</amount>", res.get(1).toString());
    }

    @Test
    public void descendantDrinkPriceQuery() {
        List<IXMLElement> res = ex(r + "drink//price");

        //XQueryExecutor.printResults(res);
    }

    @Test
    public void attr1() {
        List<IXMLElement> res = exR("drink/lemonade/@supplier");

        assertEquals(2, res.size());
        assertXMLEquals("<supplier>mother</supplier>", res, 0);
        assertXMLEquals("<supplier>mother</supplier>", res, 1);
    }

    @Test
    public void filterTest1() {
        List<IXMLElement> res = exR("drink/lemonade[price=price]");

        Element lemonade = new Element("lemonade");
        lemonade.setAttribute(new Attribute("supplier", "mother"));
        lemonade.setAttribute(new Attribute("id", "1"));
        Element price = new Element("price");
        price.addContent(new Text("$2.50"));
        Element amount = new Element("amount");
        amount.addContent(new Text("20"));
        lemonade.addContent(price);
        lemonade.addContent(amount);
        XMLElement lem = new XMLElement(lemonade);

        Element lemonade2 = new Element("lemonade");
        lemonade2.setAttribute(new Attribute("supplier", "mother"));
        lemonade2.setAttribute(new Attribute("id", "2"));
        Element price2 = new Element("price");
        price2.addContent(new Text("$3.50"));
        Element amount2 = new Element("amount");
        amount2.addContent(new Text("10"));
        lemonade2.addContent(price2);
        lemonade2.addContent(amount2);
        XMLElement lem2 = new XMLElement(lemonade2);

        assertEquals(2, res.size());
        assertEquals(lem, res.get(0));
        assertEquals(lem2, res.get(1));
    }

    @Test
    public void filterTest2() {
        List<IXMLElement> res = exR("drink/pop[@id]");

        XQueryExecutor.printResults(res);

        assertEquals(1, res.size());

    }

    private List<IXMLElement> exR(String q) {
        return super.ex(r + q);
    }
}
