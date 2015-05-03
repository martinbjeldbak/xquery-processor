package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import org.antlr.v4.runtime.misc.NotNull;

public class CondEvaluator extends XQueryEvaluator {
    public CondEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryListValue evalIdEqual(@NotNull CondIdEqualContext ctx){
        return null;
    }
    public XQueryListValue evalValEqual(@NotNull CondValEqualContext ctx){
        return null;
    }
    public XQueryListValue evalEmpty(@NotNull CondEmptyContext ctx){
        return null;
    }
    public XQueryListValue evalSomeSatis(@NotNull CondSomeSatisContext ctx){
        return null;
    }
    public XQueryListValue evalParen(@NotNull CondParenExprContext ctx){
        return null;
    }
    public XQueryListValue evalAnd(@NotNull CondAndContext ctx){
        return null;
    }
    public XQueryListValue evalOr(@NotNull CondOrContext ctx){
        return null;
    }
    public XQueryListValue evalNot(@NotNull CondNotContext ctx){
        return null;
    }

}

