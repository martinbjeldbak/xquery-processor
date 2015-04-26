package dk.martinbmadsen.xquery.executor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.parser.XPathLexer;
import dk.martinbmadsen.xquery.parser.XPathParser;
import dk.martinbmadsen.xquery.visitor.XPathVisitor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.List;

/**
 * A static class to makes executing a query using XQuery from a query in a string or file much easier.
 */
public class XPathExecutor {

    /**
     * Executes a query from the contents of a file.
     * @param queryFilePath the path to the XQuery file (absolute or relativ)
     * @return a {@link List<IXMLElement>} containing the result XML elements found as a result of executing the query
     * @throws IOException if the file cannot be found
     */
    public static List<IXMLElement> executeFromFile(String queryFilePath) throws IOException {
         return parse(new XPathLexer(new ANTLRFileStream(queryFilePath)));
    }

    /**
     * Exectutes the query given in string form.
     * @param query the query in string form
     * @return a {@link List<IXMLElement>} containing the result XML elements found as a result of executing the query
     */
    public static List<IXMLElement> executeFromString(String query) {
        return parse(new XPathLexer(new ANTLRInputStream(query)));
    }

    public static void printResults(List<IXMLElement> result) {
        System.out.println(result.size() + " results below:");
        Integer i = 0;
        for(IXMLElement c : result) {
            Debugger.result("#" + i++);
            System.out.println(c.toString());
        }
    }

    private static List<IXMLElement> parse(XPathLexer lexer) {
        XPathParser parser = new XPathParser(new CommonTokenStream(lexer));
        XPathParser.ApContext context = parser.ap(); // set entry point
        XPathVisitor visitor = new XPathVisitor();

        return visitor.visitAp(context);
    }
}
