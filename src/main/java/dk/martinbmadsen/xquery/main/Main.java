package dk.martinbmadsen.xquery.main;

import dk.martinbmadsen.xquery.listener.XQueryListener;
import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {

        try {


            XQueryLexer lexer = new XQueryLexer(new ANTLRFileStream("samples/xquery/test.xq"));
            XQueryParser parser = new XQueryParser(new CommonTokenStream(lexer));
            XQueryParser.ApContext context = parser.ap(); // set entry point

            ParseTreeWalker walker = new ParseTreeWalker();
            XQueryListener listener = new XQueryListener();
            walker.walk(listener, context);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int giveTwo() {
        return 2;
    }



}
