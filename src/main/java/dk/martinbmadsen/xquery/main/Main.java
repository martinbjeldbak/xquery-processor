package dk.martinbmadsen.xquery.main;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import org.antlr.v4.runtime.*;
import dk.martinbmadsen.xquery.visitor.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            XQueryLexer lexer = new XQueryLexer(new ANTLRFileStream("samples/xquery/test.xq"));
            XQueryParser parser = new XQueryParser(new CommonTokenStream(lexer));
            XQueryParser.ApContext context = parser.ap(); // set entry point
            XQueryVisitor visitor = new XQueryVisitor();

            List<IXMLElement> resultElems = visitor.visitAp(context);

            System.out.println(resultElems.size() + " results below:");
            Integer i = 0;
            for(IXMLElement c : resultElems) {
                Debugger.result("#" + i++);
                System.out.println(c.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int giveTwo() {
        return 2;
    }



}
