package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilterValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import org.antlr.v4.runtime.misc.NotNull;

public class XQueryVisitor extends XQueryBaseVisitor<IXQueryValue> {
    private QueryContext qc = new QueryContext();
    private ApRpEvaluator rpEval = new ApRpEvaluator(this, qc);
    private FEvaluator fEval = new FEvaluator(this, qc);

    @Override
    public XQueryListValue visitAp(@NotNull ApContext ctx) {
        return rpEval.evalAp(ctx);
    }

    @Override
    public XQueryListValue visitRpTagName(@NotNull RpTagNameContext ctx) {
        return rpEval.evalTagName(ctx);
    }

    @Override
    public XQueryListValue visitRpWildcard(@NotNull RpWildcardContext ctx) {
        return rpEval.evalWildCard();
    }

    @Override
    public XQueryListValue visitRpDot(@NotNull RpDotContext ctx) {
        return rpEval.evalDot();
    }

    @Override
    public XQueryListValue visitRpDotDot(@NotNull RpDotDotContext ctx) {
        return rpEval.evalDotDot();
    }

    @Override
    public XQueryListValue visitRpText(@NotNull RpTextContext ctx) {
        return rpEval.evalText();
    }

    @Override
    public XQueryListValue visitRpParenExpr(@NotNull RpParenExprContext ctx) {
        return rpEval.evalParen(ctx);
    }

    @Override
    public XQueryListValue visitRpSlash(@NotNull RpSlashContext ctx) {
        return rpEval.evalSlashes(ctx);
    }

    @Override
    public XQueryListValue visitRpAttr(@NotNull RpAttrContext ctx) {
        return rpEval.evalAttr(ctx);
    }

    @Override
    public XQueryListValue visitRpFilter(@NotNull RpFilterContext ctx) {
        return rpEval.evalFilter(ctx);
    }

    @Override
    public XQueryListValue visitRpConcat(@NotNull RpConcatContext ctx) {
        return rpEval.evalConcat(ctx);
    }







    @Override
    public XQueryFilterValue visitFRp(@NotNull FRpContext ctx) {
        return fEval.evalFRp(ctx);
    }

    @Override
    public IXQueryValue visitFValEqual(@NotNull FValEqualContext ctx) {
        return fEval.evalValEqual(ctx);
    }

    @Override
    public IXQueryValue visitFIdEqual(@NotNull FIdEqualContext ctx) {
        return fEval.evalIdEqual(ctx);
    }

    @Override
    public XQueryFilterValue visitFParen(@NotNull FParenContext ctx) {
        return fEval.evalParen(ctx);
    }

    @Override
    public XQueryFilterValue visitFAnd(@NotNull FAndContext ctx) {
        return fEval.evalAnd(ctx);
    }

    @Override
    public XQueryFilterValue visitFOr(@NotNull FOrContext ctx) {
        return fEval.evalOr(ctx);
    }

    @Override
    public XQueryFilterValue visitFNot(@NotNull FNotContext ctx) {
        return fEval.evalNot(ctx);
    }
}
