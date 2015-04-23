package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilterValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import org.antlr.v4.runtime.misc.NotNull;

public class XQueryVisitor2 extends XQueryBaseVisitor<IXQueryValue> {
    private QueryContext qc = new QueryContext();
    private ApRpEvaluator2 e = new ApRpEvaluator2(this, qc);

    @Override
    public XQueryListValue visitAp(@NotNull ApContext ctx) {
        return e.evalAp(ctx);
    }

    @Override
    public XQueryListValue visitRpTagName(@NotNull RpTagNameContext ctx) {
        return e.evalTagName(ctx);
    }

    @Override
    public XQueryListValue visitRpWildcard(@NotNull RpWildcardContext ctx) {
        return e.evalWildCard();
    }

    @Override
    public XQueryListValue visitRpDot(@NotNull RpDotContext ctx) {
        return e.evalDot();
    }

    @Override
    public XQueryListValue visitRpDotDot(@NotNull RpDotDotContext ctx) {
        return e.evalDotDot();
    }

    @Override
    public XQueryListValue visitRpText(@NotNull RpTextContext ctx) {
        return e.evalText();
    }

    @Override
    public XQueryListValue visitRpParenExpr(@NotNull RpParenExprContext ctx) {
        return e.evalRpParen(ctx);
    }

    @Override
    public XQueryListValue visitRpSlash(@NotNull RpSlashContext ctx) {
        return e.evalRpSlashes(ctx);
    }

    @Override
    public XQueryListValue visitRpFilter(@NotNull RpFilterContext ctx) {
        return e.evalRpFilter(ctx);
    }

    @Override
    public XQueryListValue visitRpConcat(@NotNull RpConcatContext ctx) {
        return e.evalConcat(ctx);
    }

    @Override
    public IXQueryValue visitFNot(@NotNull FNotContext ctx) {
        return super.visitFNot(ctx);
    }

    @Override
    public XQueryFilterValue visitFRp(@NotNull FRpContext ctx) {
        XQueryListValue resultR = (XQueryListValue)visit(ctx.rp());
        if(resultR.size() > 0)
            return XQueryFilterValue.trueValue();
        return XQueryFilterValue.falseValue();
    }

    @Override
    public IXQueryValue visitFParen(@NotNull FParenContext ctx) {
        return super.visitFParen(ctx);
    }

    @Override
    public IXQueryValue visitFOr(@NotNull FOrContext ctx) {
        return super.visitFOr(ctx);
    }

    @Override
    public IXQueryValue visitFEqual(@NotNull FEqualContext ctx) {
        return super.visitFEqual(ctx);
    }

    @Override
    public IXQueryValue visitFValEqual(@NotNull FValEqualContext ctx) {
        return super.visitFValEqual(ctx);
    }

    @Override
    public IXQueryValue visitFIdEqual(@NotNull FIdEqualContext ctx) {
        return super.visitFIdEqual(ctx);
    }

    @Override
    public IXQueryValue visitFAnd(@NotNull FAndContext ctx) {
        return super.visitFAnd(ctx);
    }
}
