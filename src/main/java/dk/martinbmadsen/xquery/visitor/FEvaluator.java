package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilter;
import dk.martinbmadsen.xquery.value.XQueryList;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import static dk.martinbmadsen.xquery.parser.XQueryParser.*;

public class FEvaluator extends XQueryEvaluator {
    public FEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryFilter evalFRp(FRpContext ctx) {
        XQueryList resultR = (XQueryList)visitor.visit(ctx.rp());
        if(resultR.size() > 0)
            return XQueryFilter.trueValue();
        return XQueryFilter.falseValue();
    }

    public XQueryFilter evalParen(FParenContext ctx) {
        return (XQueryFilter)visitor.visit(ctx.f());
    }

    public XQueryFilter evalAnd(FAndContext ctx) {
        XQueryFilter l = (XQueryFilter)visitor.visit(ctx.left);
        XQueryFilter r = (XQueryFilter)visitor.visit(ctx.right);
        return l.and(r);
    }

    public XQueryFilter evalOr(FOrContext ctx) {
        XQueryFilter l = (XQueryFilter)visitor.visit(ctx.left);
        XQueryFilter r = (XQueryFilter)visitor.visit(ctx.right);
        return l.or(r);
    }

    public XQueryFilter evalNot(FNotContext ctx) {
        XQueryFilter v = (XQueryFilter)visitor.visit(ctx.f());
        return v.not();
    }

    public XQueryFilter evalValEqual(FValEqualContext ctx) {
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        XQueryList r = (XQueryList)visitor.visit(ctx.right);

        for(IXMLElement x : l)
            for(IXMLElement y : r)
                if(x.equals(y))
                    return XQueryFilter.trueValue();
        return XQueryFilter.falseValue();
    }

    public XQueryFilter evalIdEqual(FIdEqualContext ctx) {
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        XQueryList r = (XQueryList)visitor.visit(ctx.right);

        for(IXMLElement x : l)
            for(IXMLElement y : r)
                if(x.equalsRef(y))
                    return XQueryFilter.trueValue();
        return XQueryFilter.falseValue();
    }
}
