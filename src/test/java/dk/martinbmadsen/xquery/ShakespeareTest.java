package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShakespeareTest extends XQueryTest {
    private String r = "doc(\"samples/xml/j_caesar.xml\")"; // root query

    @Test
    public void playground() throws JDOMException, IOException {
    }

    @Test
    public void getsARootElement() {
        List<IXMLElement> res1 = exR("/TITLE");
        List<IXMLElement> res2 = runCorrectImplementation("/TITLE");

        assertXMLEquals("<TITLE>The Tragedy of Julius Caesar</TITLE>", res1, 0);
        assertEquals(res1, res2);
        assertXPathEquals("/TITLE");
    }

    @Test
    public void loads9Personae() {
        List<IXMLElement> res = exR("/PERSONAE/PERSONA");

        assertEquals(9, res.size());
        assertXPathEquals("/PERSONAE/PERSONA");
    }

    @Test
    public void concatenation1() {
        // Concat from root
        List<IXMLElement> res = exR("/TITLE,SCNDESCR");

        assertEquals(2, res.size());
        assertXMLEquals("<TITLE>The Tragedy of Julius Caesar</TITLE>", res, 0);
        assertXMLEquals("<SCNDESCR>SCENE  Rome: the neighbourhood of Sardis: the neighbourhood of Philippi.</SCNDESCR>", res, 1);
    }

    @Test
    public void concatenation2() {
        // Concat with rps
        List<IXMLElement> res = exR("/FM/P,TITLE");

        assertEquals(5, res.size());
        assertXMLEquals("<P>Text placed in the public domain by Moby Lexical Tools, 1992.</P>", res, 0);
        assertXMLEquals("<P>SGML markup by Jon Bosak, 1992-1994.</P>", res, 1);
        assertXMLEquals("<P>XML version by Jon Bosak, 1996-1998.</P>", res, 2);
        assertXMLEquals("<P>This work may be freely copied and distributed worldwide.</P>", res, 3);
        assertXMLEquals("<TITLE>The Tragedy of Julius Caesar</TITLE>", res, 4);
    }

    @Test
    public void concatenation3() {
        // Chained concat
        List<IXMLElement> res = exR("/FM/P,TITLE,PERSONAE/TITLE");

        assertEquals(6, res.size());
        assertXMLEquals("<P>Text placed in the public domain by Moby Lexical Tools, 1992.</P>", res, 0);
        assertXMLEquals("<P>SGML markup by Jon Bosak, 1992-1994.</P>", res, 1);
        assertXMLEquals("<P>XML version by Jon Bosak, 1996-1998.</P>", res, 2);
        assertXMLEquals("<P>This work may be freely copied and distributed worldwide.</P>", res, 3);
        assertXMLEquals("<TITLE>The Tragedy of Julius Caesar</TITLE>", res, 4);
        assertXMLEquals("<TITLE>Dramatis Personae</TITLE>", res, 5);
    }

    @Test
    public void dotdot1() {
        List<IXMLElement> res = exR("/ACT/TITLE/../TITLE");

        assertEquals(5, res.size());
        assertXMLEquals("<TITLE>ACT I</TITLE>", res, 0);
        // ... the other 3 titles here
        assertXMLEquals("<TITLE>ACT V</TITLE>", res, 4);
        assertXPathEquals("/ACT/TITLE/../TITLE");
    }

    @Test
    public void dotdot2() {
        List<IXMLElement> res = exR("/ACT/TITLE/../TITLE/../../TITLE");

        assertEquals(1, res.size());
        assertXMLEquals("<TITLE>The Tragedy of Julius Caesar</TITLE>", res, 0);
        assertXPathEquals("/ACT/TITLE/../TITLE/../../TITLE");
    }

    @Test
    public void filter1() {
        List<IXMLElement> res = exR("/PERSONAE/PGROUP/PERSONA[text()]");

        assertEquals(27, res.size()); // 27 personas
        assertXPathEquals("/PERSONAE/PGROUP/PERSONA[text()]");
    }

    @Test
    public void filter2() {
        assertXPathEquals("/PERSONAE/PGROUP[PERSONA]");
    }


    private List<IXMLElement> exF() throws IOException {
        return super.exF("samples/xquery/test.xq");
    }

    private List<IXMLElement> exR(String q) {
        return super.ex(r + q);
    }

    protected List<IXMLElement> runCorrectImplementation(String query) {
        return super.runCorrectImplementation("samples/xml/j_caesar.xml", "PLAY" + query);
    }

    private void assertXPathEquals(String query) {
        assertEquals(runCorrectImplementation(query), exR(query));
    }
}
