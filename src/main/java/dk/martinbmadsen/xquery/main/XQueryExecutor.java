package dk.martinbmadsen.xquery.main;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import dk.martinbmadsen.xquery.visitor.XQueryVisitor;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.List;

public class XQueryExecutor {

    public static List<IXMLElement> executeFromFile(String queryFilePath) throws IOException {
         return parse(new XQueryLexer(new ANTLRFileStream(queryFilePath)));
    }

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
