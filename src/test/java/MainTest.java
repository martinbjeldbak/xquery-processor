import static org.junit.Assert.assertEquals;

import dk.martinbmadsen.xquery.main.Main;
import org.junit.Test;

public class MainTest {
    @Test
    public void loadsFiles() {
        Main m = new Main();

        assertEquals(2, m.giveTwo());
    }
}
