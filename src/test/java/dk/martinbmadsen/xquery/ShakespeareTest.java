package dk.martinbmadsen.xquery;

import com.pholser.junit.quickcheck.ForAll;
import com.pholser.junit.quickcheck.From;
import dk.martinbmadsen.utils.debug.QueryGenerator;
import dk.martinbmadsen.utils.debug.XQueryExecutor;
import dk.martinbmadsen.xquery.value.XQueryList;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.jdom2.JDOMException;
import org.junit.Test;
import org.junit.contrib.theories.Theories;
import org.junit.contrib.theories.Theory;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class ShakespeareTest extends XQueryTest {

    @Test
    public void playground() throws JDOMException, IOException {
        String q = "<result>{\n" +
                "for $a in doc(\"samples/xml/j_caesar.xml\")//ACT,\n" +
                "    $sc in $a//SCENE,\n" +
                "    $sp in $sc/SPEECH\n" +
                "where $sp/LINE/text() = \"Et tu, Brute! Then fall, Caesar.\"\n" +
                "return <who>{$sp/SPEAKER/text()}</who>,\n" +
                "       <when>{<act>{$a/TITLE/text()}</act>,\n" +
                "             <scene>{$sc/TITLE/text()}</scene>}\n" +
                "       </when>\n" +
                "}</result>";

        List<IXMLElement> res = ex(q);
        XQueryExecutor.printResults(res);
    }

    @Theory
    public void queryTester(@ForAll @From(QueryGenerator.class) String query) {
        System.out.println(query);
        List<IXMLElement> res1 = exR(query);
//        List<IXMLElement> res2 = runCorrectImplementation(query);
//        assertEquals(res1, res2);
    }

    @Test
    public void milestone1q2() {
        String q = "for $s in doc(\"samples/xml/j_caesar.xml\")//SPEAKER\n" +
                "return <speaks>{<who>{$s/text()}</who>,\n" +
                "                for $a in doc(\"samples/xml/j_caesar.xml\")//ACT\n" +
                "                where some $s1 in $a//SPEAKER satisfies $s1 eq $s\n" +
                "                return <when>{$a/title/text()}</when>}\n" +
                "</speaks>";

        List<IXMLElement> res = ex(q);

        //XQueryExecutor.printResults(res);
    }

    @Test
    public void xqTagName1() {
        List<IXMLElement> res = ex("<hej>{doc(\"samples/xml/j_caesar.xml\")/TITLE/text()}</hej>");

        assertXMLEquals("<hej>The Tragedy of Julius Caesar</hej>", res, 0);
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
    public void ApSlashSlash1() {
        assertXPathEquals("//PERSONA");
    }

    @Test
    public void ApSlashSlashTextGetCharacters() {
        List<IXMLElement> res = exR("//PERSONA");

        //XQueryExecutor.printResults(res);
        //XQueryExecutor.printResults(runCorrectImplementation("//PERSONA"));

        // There are 36 results... I hand counted them in the XML file
        assertEquals("JULIUS CAESAR", res.get(0).txt().toString());
        //assertEquals("Senators, Citizens, Guards, Attendants, &c.", res.get(35).toString());
        assertEquals(36, res.size());
        assertEquals(runCorrectImplementation("//PERSONA").size(), res.size());
    }

    @Test
    public void textGetsTitle() {
        List<IXMLElement> res = exR("/TITLE/text()");
        assertEquals("The Tragedy of Julius Caesar", res.get(0).toString());
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
        String r = "doc(\"samples/xml/j_caesar.xml\")";
        return super.ex(r + q);
    }

    protected List<IXMLElement> runCorrectImplementation(String query) {
        return super.runCorrectImplementation("samples/xml/j_caesar.xml", "PLAY" + query);
    }

    private void assertXPathEquals(String query) {
        assertEquals(runCorrectImplementation(query), exR(query));
    }
}
