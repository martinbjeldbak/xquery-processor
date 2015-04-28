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
    private String r = "doc(\"samples/xml/lemonade2.xml\")"; // root query

    @Test
    public void playground() {
        List<IXMLElement> res = exR("//*/amount");
        XQueryExecutor.printResults(res);
    }

    @Test
    public void lemonadePriceQuery() {
        List<IXMLElement> res = exR("/drink/lemonade/price");

        XQueryExecutor.printResults(res);

        assertEquals(2, res.size());
        assertEquals("<price>$2.50</price>", res.get(0).toString());
        assertEquals("<price>$3.50</price>", res.get(1).toString());
    }

    @Test
    public void lemonadeAmountQuery() {
        List<IXMLElement> res = exR("/drink/lemonade/amount");

        assertEquals(2, res.size());
        assertEquals("<amount>20</amount>", res.get(0).toString());
        assertEquals("<amount>10</amount>", res.get(1).toString());
    }

    @Test
    public void descendantDrinkPriceQuery() {
        List<IXMLElement> res = ex(r + "/drink//price");

        //XQueryExecutor.printResults(res);
    }

    @Test
    public void attr1() {
        List<IXMLElement> res = exR("/drink/lemonade/@supplier");

        assertEquals(2, res.size());
        assertXMLEquals("<supplier>mother</supplier>", res, 0);
        assertXMLEquals("<supplier>mother</supplier>", res, 1);
    }

    @Test
    public void filterTest1() {
        List<IXMLElement> res = exR("/drink/lemonade[price=price]");

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
        IXMLElement lem2 = new XMLElement(lemonade2);

        assertEquals(2, res.size());
        assertEquals(lem, res.get(0));
        assertEquals(lem2, res.get(1));
    }

    @Test
    public void filterTest2() {
        List<IXMLElement> res = exR("/drink/pop[@id]");

        Element pop = new Element("pop");
        pop.setAttribute(new Attribute("supplier", "store"));
        pop.setAttribute(new Attribute("id", "3"));
        Element price = new Element("price");
        price.addContent(new Text("$1.50"));
        Element amount = new Element("amount");
        amount.addContent(new Text("10"));
        pop.addContent(price);
        pop.addContent(amount);

        IXMLElement iPop = new XMLElement(pop);

        assertEquals(1, res.size());
        assertEquals(iPop, res.get(0));

    }

    @Test
    public void slashslash1() {
        List<IXMLElement> res1 = exR("//*/amount");
        List<IXMLElement> res2 = exR("//amount");

        Element amount = new Element("amount");
        amount.addContent(new Text("60"));
        IXMLElement e = new XMLElement(amount);

        assertEquals(6, res1.size());
        assertEquals(6, res2.size());
        assertEquals(res1.size(), res2.size());
        assertEquals(res1, res2);
        assertEquals(e, res1.get(4));
        assertEquals(e, res2.get(4));
    }

    private List<IXMLElement> exR(String q) {
        return super.ex(r + q);
    }
}
