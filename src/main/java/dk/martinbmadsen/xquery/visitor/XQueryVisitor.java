package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.XMLTree.XMLDocument;
import dk.martinbmadsen.xquery.debug.Debug;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import org.antlr.v4.runtime.misc.NotNull;

public class XQueryVisitor<T> extends XQueryBaseVisitor<T> {
    private XMLDocument document;

    @Override
    public T visitAp(@NotNull XQueryParser.ApContext ctx) {
        String fileName = ctx.fileName.getText().replace("\"", "");

        this.document = new XMLDocument(fileName);

        switch(ctx.slash.getType()) {
            case XQueryLexer.SLASH:
                Debug.debug("Matched /, evaluating rp");
                visit(ctx.rp());
                break;
            case XQueryLexer.SSLASH:
                Debug.debug("Matched //");
                break;
            default:
                Debug.debug("Oops, shouldn't be here");
                break;
        }

        return super.visitAp(ctx);
    }

    @Override
    public T visitRp(@NotNull XQueryParser.RpContext ctx) {
        if(ctx.tagName != null) {
            Debug.debug("Got tagName " + ctx.tagName.getText());
        }
        else if (ctx.dot != null) {
            Debug.debug("Got dot");
        }
        else if (ctx.dotdot != null) {
            Debug.debug("Got dotdot");
        }
        else {
            Debug.debug("Error: Could not match syntax: " + ctx.getText());
        }

        return super.visitRp(ctx);
    }
}
