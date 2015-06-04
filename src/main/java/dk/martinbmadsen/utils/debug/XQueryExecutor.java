package dk.martinbmadsen.utils.debug;

import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import dk.martinbmadsen.xquery.value.XQueryList;
import dk.martinbmadsen.xquery.visitor.XQueryVisitor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
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
         return execute(new ANTLRFileStream(queryFilePath));
    }

    /**
     * Exectutes the query given in string form.
     * @param query the query in string form
     * @return a {@link List<IXMLElement>} containing the result XML elements found as a result of executing the query
     */
    public static List<IXMLElement> executeFromString(String query) {
        return execute(new ANTLRInputStream(query));
    }

    public static void printResultsToFile(List<IXMLElement> res, String path) {
        SAXBuilder sb = new SAXBuilder();
        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        String xml = "";

        for(IXMLElement e : res) {
            xml += e.toCompactString();
        }

        try {
            Document doc = sb.build(new StringReader(xml));

            xout.output(doc, new FileWriter(path));

        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void printPrettyResults(List<IXMLElement> result) {
        if (result == null){
            System.out.println("Result is null");
            return;
        }
        System.out.println(result.size() + " results below:");
        Integer i = 0;
        for(IXMLElement c : result) {
            //Debugger.result("#" + i++);
            System.out.println(c.toString());
        }
    }

    public static void printCompactResults(List<IXMLElement> result) {
        if (result == null){
            System.out.println("Result is null");
            return;
        }
        System.out.println(result.size() + " results below:");
        Integer i = 0;
        for(IXMLElement c : result) {
            Debugger.result("#" + i++);
            System.out.println(c.toCompactString());
        }
    }

    private static XQueryList execute(ANTLRInputStream input) {
        XQueryLexer   lexer   = new XQueryLexer(input);
        XQueryParser  parser  = new XQueryParser(new CommonTokenStream(lexer));
        XQueryVisitor visitor = new XQueryVisitor();

        // set entry point to 'xq' non-terminal in grammar
        XQueryParser.XqContext context = parser.xq();

        return (XQueryList)visitor.visit(context);
    }
}
