package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilterValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;

import static dk.martinbmadsen.xquery.parser.XQueryParser.*;

public class FEvaluator extends XQueryEvaluator {
    public FEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryFilterValue evalFRp(FContext ctx) {
        if(!(ctx instanceof FRpContext))
            Debugger.error("context not of type FRpContext");
        FRpContext node = (FRpContext) ctx;

        XQueryListValue resultR = (XQueryListValue)visitor.visit(node.rp());
        if(resultR.size() > 0)
            return XQueryFilterValue.trueValue();
        return XQueryFilterValue.falseValue();
    }

    public XQueryFilterValue evalParen(FParenContext ctx) {
        return (XQueryFilterValue)visitor.visit(ctx.f());
    }

    public XQueryFilterValue evalAnd(FAndContext ctx) {
        XQueryFilterValue l = (XQueryFilterValue)visitor.visit(ctx.left);
        XQueryFilterValue r = (XQueryFilterValue)visitor.visit(ctx.right);

        return l.and(r);
    }

    public XQueryFilterValue evalOr(FOrContext ctx) {
        XQueryFilterValue l = (XQueryFilterValue)visitor.visit(ctx.left);
        XQueryFilterValue r = (XQueryFilterValue)visitor.visit(ctx.right);

        return l.or(r);
    }

    public XQueryFilterValue evalNot(FNotContext ctx) {
        XQueryFilterValue v = (XQueryFilterValue)visitor.visit(ctx.f());

        return v.not();
    }

    public IXQueryValue evalEquals(FEqualContext ctx) {
        switch(ctx.equal.getType()) {
            case XQueryParser.EQLS: // =
                break;
            case XQueryParser.EQUAL: // ==
                    break;
            default:
                Debugger.error("Oops, shouldn't be here");
        }

        return null;
    }
}
