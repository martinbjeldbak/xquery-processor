package dk.martinbmadsen.xquery;

import dk.martinbmadsen.utils.debug.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.junit.Test;

import java.util.List;

public class Milestone1Test extends XQueryTest {
    @Test
    public void query1() {
        String q;

        q = "<result>{\n" +
                "for $a  in doc(\"samples/xml/j_caesar.xml\")//ACT,\n" +
                "    $sc in $a//SCENE,\n" +
                "    $sp in $sc/SPEECH\n" +
                "where $sp/LINE/text() = \"Et tu, Brute! Then fall, Caesar.\"\n" +
                "return <who>{$sp/SPEAKER/text()}</who>,\n" +
                "       <when>{<act>{$a/TITLE/text()}</act>,\n" +
                "             <scene>{$sc/TITLE/text()}</scene>}\n" +
                "       </when>\n" +
                "}</result>";

        List<IXMLElement> res = ex(q);

        XQueryExecutor.printPrettyResults(res);

        assertXMLEquals("<result><who>CAESAR</who><when><act>ACT III</act><scene>SCENE I. Rome. " +
                "Before the Capitol; the Senate sitting above.</scene></when></result>", res, 0);

    }

    @Test
    public void query2() {
        String q;

        q = "<root>{" +
                "for $s in doc(\"samples/xml/j_caesar.xml\")//SPEAKER\n" +
                "return <speaks>{<who>{$s/text()}</who>,\n" +
                "                for $a in doc(\"samples/xml/j_caesar.xml\")//ACT\n" +
                "                where some $s1 in $a//SPEAKER satisfies $s1 eq $s\n" +
                "                return <when>{$a/TITLE/text()}</when>}\n" +
                "</speaks>" +
            "}</root>";

        List<IXMLElement> res = ex(q);

        XQueryExecutor.printResultsToFile(res, "samples/output/query2.xml");

        //assertEquals(798, res.size());
        //assertXMLEquals("<speaks><who>FLAVIUS</who><when>ACT I</when></speaks>", res, 0);
        //assertXMLEquals("<speaks><who>OCTAVIUS</who><when>ACT IV</when><when>ACT V</when></speaks>", res, 797);
    }

    @Test
    public void query3() {
        String q;

        q = "<root>{  for $s in doc(\"samples/xml/j_caesar.xml\")//SCENE\n" +
                "return\n" +
                "<scenes>{\n" +
                "<scene>{$s/TITLE/text()}</scene>,\n" +
                "for $a in doc(\"samples/xml/j_caesar.xml\")//ACT\n" +
                "where some $s1 in $a//SCENE satisfies $s1 eq $s and $a/TITLE/text() = \"ACT II\"\n" +
                "return <act>{$a/TITLE/text()}</act>\n" +
                "}\n" +
                "</scenes>}</root>";
        List<IXMLElement> res = ex(q);

        XQueryExecutor.printPrettyResults(res);
        XQueryExecutor.printResultsToFile(res, "samples/output/query3.xml");

    }

    @Test
    public void query4() {
        String q;

        q = "<acts> {\n" +
                "for $a in doc(\"samples/xml/j_caesar.xml\")//ACT\n" +
                "where empty(\n" +
                "for $sp in $a/SCENE/SPEECH/SPEAKER\n" +
                "where $sp/text() = \"CASCA\"\n" +
                "return <speaker>{$sp/text()}</speaker>\n" +
                ")\n" +
                "return <act>{$a/TITLE/text()}</act>\n" +
                "}</acts>";

        List<IXMLElement> res = ex(q);

        XQueryExecutor.printPrettyResults(res);
    }
}
