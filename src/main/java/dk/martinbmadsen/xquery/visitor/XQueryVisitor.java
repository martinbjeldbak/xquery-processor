package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilter;
import dk.martinbmadsen.xquery.value.XQueryList;
import org.antlr.v4.runtime.misc.NotNull;

public class XQueryVisitor extends XQueryBaseVisitor<IXQueryValue> {
    private QueryContext qc = new QueryContext();
    private ApRpEvaluator rpEval = new ApRpEvaluator(this, qc);
    private FEvaluator fEval = new FEvaluator(this, qc);
    private XqEvaluator xqEval = new XqEvaluator(this, qc);
    private FLWREvaluator FLWREval = new FLWREvaluator(this, qc);
    private CondEvaluator condEval = new CondEvaluator(this, qc);

    /** APS **/

    @Override
    public XQueryList visitAp(@NotNull ApContext ctx) {
        return rpEval.evalAp(ctx);
    }

    /** RPS **/

    @Override
    public XQueryList visitRpTagName(@NotNull RpTagNameContext ctx) {
        return rpEval.evalTagName(ctx);
    }

    @Override
    public XQueryList visitRpWildcard(@NotNull RpWildcardContext ctx) {
        return rpEval.evalWildCard();
    }

    @Override
    public XQueryList visitRpDot(@NotNull RpDotContext ctx) {
        return rpEval.evalDot();
    }

    @Override
    public XQueryList visitRpDotDot(@NotNull RpDotDotContext ctx) {
        return rpEval.evalDotDot();
    }

    @Override
    public XQueryList visitRpText(@NotNull RpTextContext ctx) {
        return rpEval.evalText();
    }

    @Override
    public XQueryList visitRpParenExpr(@NotNull RpParenExprContext ctx) {
        return rpEval.evalParen(ctx);
    }

    @Override
    public XQueryList visitRpSlash(@NotNull RpSlashContext ctx) {
        return rpEval.evalSlashes(ctx);
    }

    @Override
    public XQueryList visitRpAttr(@NotNull RpAttrContext ctx) {
        return rpEval.evalAttr(ctx);
    }

    @Override
    public XQueryList visitRpFilter(@NotNull RpFilterContext ctx) {
        return rpEval.evalFilter(ctx);
    }

    @Override
    public XQueryList visitRpConcat(@NotNull RpConcatContext ctx) {
        return rpEval.evalConcat(ctx);
    }

    /** FILTERS **/

    @Override
    public XQueryFilter visitFRp(@NotNull FRpContext ctx) {
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
    public XQueryFilter visitFParen(@NotNull FParenContext ctx) {
        return fEval.evalParen(ctx);
    }

    @Override
    public XQueryFilter visitFAnd(@NotNull FAndContext ctx) {
        return fEval.evalAnd(ctx);
    }

    @Override
    public XQueryFilter visitFOr(@NotNull FOrContext ctx) {
        return fEval.evalOr(ctx);
    }

    @Override
    public XQueryFilter visitFNot(@NotNull FNotContext ctx) {
        return fEval.evalNot(ctx);
    }

    /** XQ **/

    @Override
    public IXQueryValue visitXqVar(@NotNull XqVarContext ctx) {
        return xqEval.evalVar(ctx);
    }

    @Override
    public IXQueryValue visitXqStringConstant(@NotNull XqStringConstantContext ctx) {
        return xqEval.evalStringConstant(ctx);
    }

    @Override
    public IXQueryValue visitXqAp(@NotNull XqApContext ctx) {
        return xqEval.evalAp(ctx);
    }

    @Override
    public IXQueryValue visitXqParenExpr(@NotNull XqParenExprContext ctx) {
        return xqEval.evalParen(ctx);
    }

    @Override
    public IXQueryValue visitXqConcat(@NotNull XqConcatContext ctx) {
        return xqEval.evalConcat(ctx);
    }

    @Override
    public IXQueryValue visitXqSlash(@NotNull XqSlashContext ctx) {
        return xqEval.evalSlashes(ctx);
    }

    @Override
    public IXQueryValue visitXqTagName(@NotNull XqTagNameContext ctx) {
        return xqEval.evalTagname(ctx);
    }

    @Override
    public IXQueryValue visitXqFLWR(@NotNull XqFLWRContext ctx) {
        return xqEval.evalFLWR(ctx);
    }

    @Override
    public IXQueryValue visitXqLet(@NotNull XqLetContext ctx) {
        return xqEval.evalLet(ctx);
    }

    /** FLOWR CLAUSES **/

    @Override
    public IXQueryValue visitForClause(@NotNull ForClauseContext ctx) {
        return FLWREval.evalFor(ctx);
    }

    @Override
    public IXQueryValue visitLetClause(@NotNull LetClauseContext ctx) {
        FLWREval.evalLet(ctx);
        return null;
    }

    @Override
    public IXQueryValue visitWhereClause(@NotNull WhereClauseContext ctx) {
        return FLWREval.evalWhere(ctx);
    }

    @Override
    public IXQueryValue visitReturnClause(@NotNull ReturnClauseContext ctx) {
        return FLWREval.evalReturn(ctx);
    }

    /** CONDITION CLAUSES **/

    @Override
    public IXQueryValue visitCondValEqual(@NotNull CondValEqualContext ctx) {
        return condEval.evalValEqual(ctx);
    }

    @Override
    public IXQueryValue visitCondIdEqual(@NotNull CondIdEqualContext ctx) {
        return condEval.evalIdEqual(ctx);
    }

    @Override
    public IXQueryValue visitCondEmpty(@NotNull CondEmptyContext ctx) {
        return condEval.evalEmpty(ctx);
    }

    @Override
    public IXQueryValue visitCondSomeSatis(@NotNull CondSomeSatisContext ctx) {
        return condEval.evalSomeSatis(ctx);
    }

    @Override
    public IXQueryValue visitCondParenExpr(@NotNull CondParenExprContext ctx) {
        return condEval.evalParen(ctx);
    }

    @Override
    public IXQueryValue visitCondAnd(@NotNull CondAndContext ctx) {
        return condEval.evalAnd(ctx);
    }

    @Override
    public IXQueryValue visitCondOr(@NotNull CondOrContext ctx) {
        return condEval.evalOr(ctx);
    }

    @Override
    public IXQueryValue visitCondNot(@NotNull CondNotContext ctx) {
        return condEval.evalNot(ctx);
    }
}
