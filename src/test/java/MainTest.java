import static org.junit.Assert.assertEquals;

import dk.martinbmadsen.xquery.main.Main;
import dk.martinbmadsen.xquery.parser.XQueryBaseListener;
import org.junit.Test;

public class MainTest {
    @Test
    public void loadsFiles() {
        Main m = new Main();

        XQueryBaseListener bl = new XQueryBaseListener();
        assertEquals(2, m.giveTwo());
    }
}
