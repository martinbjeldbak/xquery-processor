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

    /** APS **/

    @Override
    public XQueryListValue visitAp(@NotNull ApContext ctx) {
        return rpEval.evalAp(ctx);
    }

    /** RPS **/

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

    /** FILTERS **/

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

    /** FLOWR CLAUSES **/

    @Override
    public IXQueryValue visitForClause(@NotNull ForClauseContext ctx) {
        return super.visitForClause(ctx);
    }

    @Override
    public IXQueryValue visitLetClause(@NotNull LetClauseContext ctx) {
        return super.visitLetClause(ctx);
    }

    @Override
    public IXQueryValue visitWhereClause(@NotNull WhereClauseContext ctx) {
        return super.visitWhereClause(ctx);
    }

    @Override
    public IXQueryValue visitReturnClause(@NotNull ReturnClauseContext ctx) {
        return super.visitReturnClause(ctx);
    }

    /** CONDITION CLAUSES **/

    @Override
    public IXQueryValue visitCondValEqual(@NotNull CondValEqualContext ctx) {
        return super.visitCondValEqual(ctx);
    }

    @Override
    public IXQueryValue visitCondEmpty(@NotNull CondEmptyContext ctx) {
        return super.visitCondEmpty(ctx);
    }

    @Override
    public IXQueryValue visitCondSomeSatis(@NotNull CondSomeSatisContext ctx) {
        return super.visitCondSomeSatis(ctx);
    }

    @Override
    public IXQueryValue visitCondIdEqual(@NotNull CondIdEqualContext ctx) {
        return super.visitCondIdEqual(ctx);
    }

    @Override
    public IXQueryValue visitCondParenExpr(@NotNull CondParenExprContext ctx) {
        return super.visitCondParenExpr(ctx);
    }

    @Override
    public IXQueryValue visitCondAnd(@NotNull CondAndContext ctx) {
        return super.visitCondAnd(ctx);
    }

    @Override
    public IXQueryValue visitCondNot(@NotNull CondNotContext ctx) {
        return super.visitCondNot(ctx);
    }

    @Override
    public IXQueryValue visitCondOr(@NotNull CondOrContext ctx) {
        return super.visitCondOr(ctx);
    }

    /** XQ **/

    @Override
    public IXQueryValue visitXqParenExpr(@NotNull XqParenExprContext ctx) {
        return super.visitXqParenExpr(ctx);
    }

    @Override
    public IXQueryValue visitXqFLWR(@NotNull XqFLWRContext ctx) {
        return super.visitXqFLWR(ctx);
    }

    @Override
    public IXQueryValue visitXqTagName(@NotNull XqTagNameContext ctx) {
        return super.visitXqTagName(ctx);
    }

    @Override
    public IXQueryValue visitXqVar(@NotNull XqVarContext ctx) {
        return super.visitXqVar(ctx);
    }

    @Override
    public IXQueryValue visitXqSlash(@NotNull XqSlashContext ctx) {
        return super.visitXqSlash(ctx);
    }

    @Override
    public IXQueryValue visitXqLet(@NotNull XqLetContext ctx) {
        return super.visitXqLet(ctx);
    }

    @Override
    public IXQueryValue visitXqStringConstant(@NotNull XqStringConstantContext ctx) {
        return super.visitXqStringConstant(ctx);
    }

    @Override
    public IXQueryValue visitXqAp(@NotNull XqApContext ctx) {
        return super.visitXqAp(ctx);
    }

    @Override
    public IXQueryValue visitXqConcat(@NotNull XqConcatContext ctx) {
        return super.visitXqConcat(ctx);
    }
}
