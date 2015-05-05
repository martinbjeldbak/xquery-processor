package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilterValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import static dk.martinbmadsen.xquery.parser.XQueryParser.*;

public class FEvaluator extends XQueryEvaluator {
    public FEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryFilterValue evalFRp(FRpContext ctx) {
        XQueryListValue resultR = (XQueryListValue)visitor.visit(ctx.rp());
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

    public XQueryFilterValue evalValEqual(FValEqualContext ctx) {
        XQueryListValue l = (XQueryListValue)visitor.visit(ctx.left);
        XQueryListValue r = (XQueryListValue)visitor.visit(ctx.right);

        for(IXMLElement x : l)
            for(IXMLElement y : r)
                if(x.equals(y))
                    return XQueryFilterValue.trueValue();
        return XQueryFilterValue.falseValue();
    }

    public XQueryFilterValue evalIdEqual(FIdEqualContext ctx) {
        XQueryListValue l = (XQueryListValue)visitor.visit(ctx.left);
        XQueryListValue r = (XQueryListValue)visitor.visit(ctx.right);

        for(IXMLElement x : l)
            for(IXMLElement y : r)
                if(x.equalsRef(y))
                    return XQueryFilterValue.trueValue();
        return XQueryFilterValue.falseValue();
    }
}
