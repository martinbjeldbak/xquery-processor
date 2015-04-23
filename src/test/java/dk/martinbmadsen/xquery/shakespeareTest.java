package dk.martinbmadsen.xquery;

import dk.martinbmadsen.xquery.executor.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShakespeareTest extends XQueryTest {
    private String r = "doc(\"samples/xml/j_caesar.xml\")/"; // root query

    @Test
    public void playground() {
        List<IXMLElement> res = exR("ACT/TITLE/../TITLE");
        XQueryExecutor.printResults(res);
    }

    @Test
    public void getsARootElement() {
        assertXMLEquals("<TITLE>The Tragedy of Julius Caesar</TITLE>", exR("TITLE"), 0);
    }

    @Test
    public void loads9Personae() {
        List<IXMLElement> res = exR("PERSONAE/PERSONA");

        assertEquals(9, res.size());
    }

    @Test
    public void slashSlash() throws IOException {
        // TODO: Implement // semantics
        //XQueryExecutor.printResults(exF());
    }

    @Test
    public void concatenation1() {
        // Concat from root
        List<IXMLElement> res = exR("TITLE,SCNDESCR");

        assertEquals(2, res.size());
        assertXMLEquals("<TITLE>The Tragedy of Julius Caesar</TITLE>", res, 0);
        assertXMLEquals("<SCNDESCR>SCENE  Rome: the neighbourhood of Sardis: the neighbourhood of Philippi.</SCNDESCR>", res, 1);
    }

    @Test
    public void concatenation2() {
        // Concat with rps
        List<IXMLElement> res = exR("FM/P,TITLE");

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
        List<IXMLElement> res = exR("FM/P,TITLE,PERSONAE/TITLE");

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
        List<IXMLElement> res = exR("ACT/TITLE/../TITLE");

        assertEquals(5, res.size());
        assertXMLEquals("<TITLE>ACT I</TITLE>", res, 0);
        // ... the other 3 titles here
        assertXMLEquals("<TITLE>ACT V</TITLE>", res, 4);
    }

    @Test
    public void dotdot2() {
        // TODO: Fails because unique() is not implemented yet
        List<IXMLElement> res = exR("ACT/TITLE/../TITLE/../../TITLE");
        /*
        assertEquals(1, res.size());
        assertXMLEquals("<TITLE>The Tragedy of Julius Caesar</TITLE>", res, 0);
        */
    }

    @Test
    public void filter1() {
        List<IXMLElement> res = exR("PERSONAE/PGROUP/PERSONA[text()]");

        assertEquals(27, res.size()); // 27 personas
    }

    private List<IXMLElement> exF() throws IOException {
        return super.exF("samples/xquery/test.xq");
    }

    private List<IXMLElement> exR(String q) {
        return super.ex(r + q);
    }
}
