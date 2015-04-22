package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LemonadeTest extends XQueryTest {
    private String r = "doc(\"samples/xml/lemonade2.xml\")/"; // root query

    @Test
    public void lemonadePriceQuery() {
        List<IXMLElement> res = ex(r + "drink/lemonade/price");

        assertEquals(1, res.size());
        assertEquals("<price>$2.50</price>", res.get(0).toString());
    }

    @Test
    public void lemonadeAmountQuery() {
        List<IXMLElement> res = ex(r + "drink/lemonade/amount");

        assertEquals(1, res.size());
        assertEquals("<amount>20</amount>", res.get(0).toString());
    }

    @Test
    public void descendantDrinkPriceQuery() {
        ex(r + "drink//price");
    }
}
