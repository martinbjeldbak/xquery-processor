package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.executor.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
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

    private List<IXMLElement> exR(String q) {
        return super.ex(r + q);
    }
}
