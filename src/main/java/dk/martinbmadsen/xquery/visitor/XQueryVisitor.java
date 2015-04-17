package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.XMLTree.XMLDocument;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import org.antlr.v4.runtime.misc.NotNull;

public class XQueryVisitor<T> extends XQueryBaseVisitor<T> {

    @Override
    public T visitAp(@NotNull XQueryParser.ApContext ctx) {
        String fileName = ctx.fileName.getText().replace("'", "");

        XMLDocument document = new XMLDocument(fileName);



        visit(ctx.rp());

        switch(ctx.slash.getType()) {
            case XQueryLexer.SLASH:
                System.out.println("Matched /");
                break;
            case XQueryLexer.SSLASH:
                System.out.println("Matched //");
                break;
            default:
                System.out.println("Oops, shouldn't be here");
                break;
        }


        return super.visitAp(ctx);
    }
}
