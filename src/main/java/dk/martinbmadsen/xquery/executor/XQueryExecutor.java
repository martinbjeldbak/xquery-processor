package dk.martinbmadsen.xquery.executor;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import dk.martinbmadsen.xquery.visitor.XQueryVisitor;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.List;

/**
 * A static class to makes executing a query using XQuery from a query in a string or file much easier.
 */
public class XQueryExecutor {

    /**
     * Executes a query from the contents of a file.
     * @param queryFilePath the path to the XQuery file (absolute or relativ)
     * @return a {@link List<IXMLElement>} containing the result XML elements found as a result of executing the query
     * @throws IOException if the file cannot be found
     */
    public static List<IXMLElement> executeFromFile(String queryFilePath) throws IOException {
         return parse(new XQueryLexer(new ANTLRFileStream(queryFilePath)));
    }

    /**
     * Exectutes the query given in string form.
     * @param query the query in string form
     * @return a {@link List<IXMLElement>} containing the result XML elements found as a result of executing the query
     */
    public static List<IXMLElement> executeFromString(String query) {
        return parse(new XQueryLexer(new ANTLRInputStream(query)));
    }

    private static List<IXMLElement> parse(XQueryLexer lexer) {
        XQueryParser parser = new XQueryParser(new CommonTokenStream(lexer));
        XQueryParser.ApContext context = parser.ap(); // set entry point
        XQueryVisitor visitor = new XQueryVisitor();

        return visitor.visitAp(context);
    }
}
