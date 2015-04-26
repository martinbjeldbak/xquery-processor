package dk.martinbmadsen.xquery.xmltree;

import org.jdom2.Element;
import org.jdom2.Text;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLElementTest {
    @Test
    public void equalsTest1() {
        Element e = new Element("test");
        e.addContent(new Text("Content of test node"));

        XMLElement a = new XMLElement(e);

        XMLElement aCloned = new XMLElement(a.getElement().clone());

        assertEquals(true, a.equalsRef(a));
        assertEquals(false, a.equalsRef(aCloned));

        assertEquals(true, a.equals(aCloned));
    }

    @Test
    public void equalsTest2() {
        Element e = new Element("test");
        Element e2 = new Element("childOfTest");
        e.addContent(new Text("Content of test node"));
        e2.addContent(new Text("yoloswaggins"));
        e.addContent(e2);

        XMLElement a = new XMLElement(e);

        XMLElement aCloned = new XMLElement(a.getElement().clone());

        assertEquals(true, a.equalsRef(a));
        assertEquals(false, a.equalsRef(aCloned));

        assertEquals(true, a.equals(aCloned));
    }
}
