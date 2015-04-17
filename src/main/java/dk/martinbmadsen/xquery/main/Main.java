package dk.martinbmadsen.xquery.main;

import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;;
import org.antlr.v4.runtime.*;
import dk.martinbmadsen.xquery.visitor.*;

public class Main {
    public static void main(String[] args) {

        try {
            XQueryLexer lexer = new XQueryLexer(new ANTLRFileStream("samples/xquery/test.xq"));
            XQueryParser parser = new XQueryParser(new CommonTokenStream(lexer));
            XQueryParser.ApContext context = parser.ap(); // set entry point
            XQueryVisitor visitor = new XQueryVisitor();

            visitor.visitAp(context);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int giveTwo() {
        return 2;
    }



}
