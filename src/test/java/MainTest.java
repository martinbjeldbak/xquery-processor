import static org.junit.Assert.assertEquals;

import dk.martinbmadsen.xquery.main.Main;
import org.junit.Test;

/**
 * Created by martin on 09/04/15.
 */
public class MainTest {
    @Test
    public void loadsFiles() {
        Main m = new Main();

        assertEquals(2, m.giveTwo());
    }
}
