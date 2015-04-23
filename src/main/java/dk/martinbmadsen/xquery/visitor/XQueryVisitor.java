package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

import dk.martinbmadsen.xquery.parser.XQueryParser.*;

public class XQueryVisitor extends XQueryBaseVisitor<List<IXMLElement>> {
    private Evaluator e = new Evaluator(this);

    @Override
    public List<IXMLElement> visitAp(@NotNull ApContext ctx) {
        return e.evalAp(ctx);
    }

    @Override
    public List<IXMLElement> visitRpTagName(@NotNull RpTagNameContext ctx) {
        return e.evalTagName(ctx);
    }

    @Override
    public List<IXMLElement> visitRpWildcard(@NotNull RpWildcardContext ctx) {
        return e.evalWildCard();
    }

    @Override
    public List<IXMLElement> visitRpDot(@NotNull RpDotContext ctx) {
        return e.evalDot();
    }

    @Override
    public List<IXMLElement> visitRpDotDot(@NotNull RpDotDotContext ctx) {
        return e.evalDotDot();
    }

    @Override
    public List<IXMLElement> visitRpText(@NotNull RpTextContext ctx) {
        return e.evalText();
    }

    @Override
    public List<IXMLElement> visitRpParenExpr(@NotNull RpParenExprContext ctx) {
        return e.evalRpParen(ctx);
    }

    @Override
    public List<IXMLElement> visitRpSlash(@NotNull RpSlashContext ctx) {
        return e.evalRpSlashes(ctx);
    }

    @Override
    public List<IXMLElement> visitRpFilter(@NotNull RpFilterContext ctx) {
        return e.evalRpFilter(ctx);
    }

    @Override
    public List<IXMLElement> visitRpConcat(@NotNull RpConcatContext ctx) {
        return e.evalConcat(ctx);
    }

    @Override
    public List<IXMLElement> visitF(@NotNull FContext ctx) {
        return super.visitF(ctx);
    }
}
