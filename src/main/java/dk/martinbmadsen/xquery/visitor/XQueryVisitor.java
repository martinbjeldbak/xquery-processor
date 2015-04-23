package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public class XQueryVisitor extends XQueryBaseVisitor<List<IXMLElement>> {
    private ApRpEvaluator e = new ApRpEvaluator(this);

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
    public List<IXMLElement> visitFNot(@NotNull FNotContext ctx) {
        return super.visitFNot(ctx);
    }

    @Override
    public List<IXMLElement> visitFRp(@NotNull FRpContext ctx) {
        List<IXMLElement> resultR = visit(ctx.rp());
        //return resultR.size() > 0;
        return super.visitFRp(ctx);
    }

    @Override
    public List<IXMLElement> visitFParen(@NotNull FParenContext ctx) {
        return super.visitFParen(ctx);
    }

    @Override
    public List<IXMLElement> visitFOr(@NotNull FOrContext ctx) {
        return super.visitFOr(ctx);
    }

    @Override
    public List<IXMLElement> visitFEqual(@NotNull FEqualContext ctx) {
        return super.visitFEqual(ctx);
    }

    @Override
    public List<IXMLElement> visitFValEqual(@NotNull FValEqualContext ctx) {
        return super.visitFValEqual(ctx);
    }

    @Override
    public List<IXMLElement> visitFIdEqual(@NotNull FIdEqualContext ctx) {
        return super.visitFIdEqual(ctx);
    }

    @Override
    public List<IXMLElement> visitFAnd(@NotNull FAndContext ctx) {
        return super.visitFAnd(ctx);
    }
}
