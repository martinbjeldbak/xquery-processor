import dk.martinbmadsen.xquery.main.Main;
import dk.martinbmadsen.xquery.main.XQueryExecutor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LemonadeTest {
    @Test
    public void loadsFiles() {
        XQueryExecutor.executeFromString("" +
                "doc(\"samples/xml/lemonade2.xml\")/drink/lemonade/price" +
                "");
    }
}
