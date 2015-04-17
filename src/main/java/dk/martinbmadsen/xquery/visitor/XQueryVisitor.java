package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by martin on 16/04/15.
 */
public class XQueryVisitor extends XQueryBaseVisitor {
    @Override
    public Object visitAp(@NotNull XQueryParser.ApContext ctx) {
        System.out.println(ctx.getText());
        return super.visitAp(ctx);
    }
}
