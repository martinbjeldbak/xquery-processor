package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilter;
import dk.martinbmadsen.xquery.value.XQueryList;
import org.antlr.v4.runtime.misc.NotNull;

public class CondEvaluator extends XQueryEvaluator {
    public CondEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryFilter evalIdEqual(@NotNull CondIdEqualContext ctx){
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        XQueryList r = (XQueryList)visitor.visit(ctx.left);
        return l.equalsId(r);
    }
    public XQueryFilter evalValEqual(@NotNull CondValEqualContext ctx){
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        XQueryList r = (XQueryList)visitor.visit(ctx.left);
        return l.equalsVal(r);
    }

    public XQueryFilter evalEmpty(@NotNull CondEmptyContext ctx){
        return null;
    }
    public XQueryFilter evalSomeSatis(@NotNull CondSomeSatisContext ctx){
        return null;
    }
    public XQueryFilter evalParen(@NotNull CondParenExprContext ctx){
        return null;
    }
    public XQueryFilter evalAnd(@NotNull CondAndContext ctx){
        return null;
    }
    public XQueryFilter evalOr(@NotNull CondOrContext ctx){
        return null;
    }
    public XQueryFilter evalNot(@NotNull CondNotContext ctx){
        return null;
    }

}

