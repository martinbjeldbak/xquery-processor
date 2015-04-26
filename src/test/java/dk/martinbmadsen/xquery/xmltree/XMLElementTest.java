package dk.martinbmadsen.xquery.xmltree;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Text;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLElementTest {
    @Test
    public void equalsTest1() {
        Element e = new Element("test");
        Element e2 = new Element("childOfTest");
        e.addContent(new Text("Content of test node"));
        e2.addContent(new Text("yoloswaggins"));

        XMLElement a = new XMLElement(e);
        XMLElement b = new XMLElement(e);

        assertEquals(true, a.equalsRef(a));
        assertEquals(true, b.equalsRef(b));
        assertEquals(true, a.equalsRef(b));

        assertEquals(true, a.equals(b));
    }

    /**
     * Copy, except there is a different text of a subelement
     */
    @Test
    public void equalsTest3() {
        //
        Element e = new Element("test");
        Element e2 = new Element("childOfTest");
        e.addContent(new Text("Content of test node"));
        e2.addContent(new Text("yoloswaggins"));
        e.addContent(e2);
        XMLElement a = new XMLElement(e);

        Element eCopy = new Element("test");
        Element e2Copy = new Element("childOfTest");
        eCopy.addContent(new Text("Content of test node"));
        e2Copy.addContent(new Text("thisidfifferent"));
        eCopy.addContent(e2Copy);
        XMLElement almostCopy = new XMLElement(eCopy);

        assertEquals(true, a.equalsRef(a));
        assertEquals(true, almostCopy.equalsRef(almostCopy));
        assertEquals(false, a.equalsRef(almostCopy));
        assertEquals(false, a.equals(almostCopy));
    }

    /**
     * Exact copy
     */
    @Test
    public void equalsTest4() {
        Element e = new Element("test");
        Element e2 = new Element("childOfTest");
        e.addContent(new Text("Content of test node"));
        e2.addContent(new Text("yoloswaggins"));
        e.addContent(e2);
        XMLElement a = new XMLElement(e);

        Element eCopy = new Element("test");
        Element e2Copy = new Element("childOfTest");
        eCopy.addContent(new Text("Content of test node"));
        e2Copy.addContent(new Text("yoloswaggins"));
        eCopy.addContent(e2Copy);
        XMLElement aCopy = new XMLElement(eCopy);

        assertEquals(true, a.equalsRef(a));
        assertEquals(true, aCopy.equalsRef(aCopy));
        assertEquals(true, a.equals(aCopy));
        assertEquals(false, a.equalsRef(aCopy));

    }

    /**
     * Copy has an attribute, so they shouldn't be equal
     */
    @Test
    public void equalsTest5() {
        Element e = new Element("test");
        Element e2 = new Element("childOfTest");
        e.addContent(new Text("Content of test node"));
        e2.addContent(new Text("yoloswaggins"));
        e.addContent(e2);
        XMLElement a = new XMLElement(e);

        Element eCopy = new Element("test");
        Element e2Copy = new Element("childOfTest");
        eCopy.addContent(new Text("Content of test node"));
        e2Copy.addContent(new Text("yoloswaggins"));
        e2Copy.setAttribute(new Attribute("Atrr1", "Value1"));
        eCopy.addContent(e2Copy);
        XMLElement aCopy = new XMLElement(eCopy);

        assertEquals(true, a.equalsRef(a));
        assertEquals(true, aCopy.equalsRef(aCopy));
        assertEquals(false, a.equalsRef(aCopy));
        assertEquals(false, a.equals(aCopy));
    }
}
