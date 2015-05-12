package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.VarEnvironment;
import dk.martinbmadsen.xquery.value.XQueryFilter;
import dk.martinbmadsen.xquery.value.XQueryList;
import org.antlr.v4.runtime.misc.NotNull;

public class CondEvaluator extends XQueryEvaluator {
    public CondEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryFilter evalIdEqual(@NotNull CondIdEqualContext ctx){
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        XQueryList r = (XQueryList)visitor.visit(ctx.right);
        return l.equalsId(r);
    }
    public XQueryFilter evalValEqual(@NotNull CondValEqualContext ctx){
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        XQueryList r = (XQueryList)visitor.visit(ctx.right);
        return l.equalsVal(r);
    }

    public XQueryFilter evalEmpty(@NotNull CondEmptyContext ctx){
        XQueryList res = (XQueryList)visitor.visit(ctx.xq());
        return res.empty();
    }

    public XQueryFilter evalSomeSatis(@NotNull CondSomeSatisContext ctx){
        VarEnvironment ve = new VarEnvironment();

        for(int i = 0; i < ctx.xq().size(); i++) {
            XQueryList res = (XQueryList)visitor.visit(ctx.xq(i));
            ve.put(ctx.Var(i).getText(), res);
        }

        qc.pushVarEnv(ve);

        XQueryFilter res = (XQueryFilter)visitor.visit(ctx.cond());

        qc.popVarEnv();

        return res;
    }

    public XQueryFilter evalParen(@NotNull CondParenExprContext ctx){
        return (XQueryFilter)visitor.visit(ctx.cond());
    }

    public XQueryFilter evalAnd(@NotNull CondAndContext ctx){
        XQueryFilter l = (XQueryFilter)visitor.visit(ctx.left);
        XQueryFilter r = (XQueryFilter)visitor.visit(ctx.right);
        return l.and(r);
    }

    public XQueryFilter evalOr(@NotNull CondOrContext ctx){
        XQueryFilter l = (XQueryFilter)visitor.visit(ctx.left);
        XQueryFilter r = (XQueryFilter)visitor.visit(ctx.right);
        return l.or(r);
    }

    public XQueryFilter evalNot(@NotNull CondNotContext ctx){
        XQueryFilter res = (XQueryFilter)visitor.visit(ctx.cond());
        return res.not();
    }

}

