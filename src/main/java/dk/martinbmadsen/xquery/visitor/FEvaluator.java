package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilterValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;

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
        // Save context XML element n
        IXMLElement n = qc.peekContextElement();

        XQueryFilterValue l = (XQueryFilterValue)visitor.visit(ctx.left);

        // Push element n back onto our context stack (since r also has to be evaluated from n)
        qc.pushContextElement(n);

        XQueryFilterValue r = (XQueryFilterValue)visitor.visit(ctx.right);

        return l.and(r);
    }

    public XQueryFilterValue evalOr(FOrContext ctx) {
        // Save context XML element n
        IXMLElement n = qc.peekContextElement();

        XQueryFilterValue l = (XQueryFilterValue)visitor.visit(ctx.left);

        // Push element n back onto our context stack (since r also has to be evaluated from n)
        qc.pushContextElement(n);

        XQueryFilterValue r = (XQueryFilterValue)visitor.visit(ctx.right);

        return l.or(r);
    }

    public XQueryFilterValue evalNot(FNotContext ctx) {
        XQueryFilterValue v = (XQueryFilterValue)visitor.visit(ctx.f());

        return v.not();
    }

    public XQueryFilterValue evalEquals(FEqualContext ctx) {
        XQueryFilterValue val;

        switch(ctx.equal.getType()) {
            case XQueryParser.EQLS: // =, value equality
                val = evalValEqual(ctx);
                break;
            case XQueryParser.EQUAL: // ==, Identity equal
                val = evalIdEqual(ctx);
                break;
            default:
                val = XQueryFilterValue.falseValue();
                Debugger.error("Oops, shouldn't be here (FEqualsContext)");
        }
        return val;
    }

    private XQueryFilterValue evalValEqual(FEqualContext ctx) {
        // Save context XML element n
        IXMLElement n = qc.peekContextElement();
        XQueryListValue l = (XQueryListValue)visitor.visit(ctx.left);

        // Push element n back onto our context stack (since r also has to be evaluated from n)
        qc.pushContextElement(n);

        XQueryListValue r = (XQueryListValue)visitor.visit(ctx.right);

        XQueryListValue res = new XQueryListValue(1);

        for(IXMLElement x : l) {
            for(IXMLElement y : r) {
                if(x.equals(y)) {
                    res.add(y);
                    break;
                }
            }
        }

        if(res.size() > 0)
            return XQueryFilterValue.trueValue();
        return XQueryFilterValue.falseValue();
    }

    private XQueryFilterValue evalIdEqual(FEqualContext ctx) {
        // Save context XML element n
        IXMLElement n = qc.peekContextElement();
        XQueryListValue l = (XQueryListValue)visitor.visit(ctx.left);

        // Push element n back onto our context stack (since r also has to be evaluated from n)
        qc.pushContextElement(n);

        XQueryListValue r = (XQueryListValue)visitor.visit(ctx.right);

        XQueryListValue res = new XQueryListValue(1);

        for(IXMLElement x : l) {
            for(IXMLElement y : r) {
                if(x.equalsRef(y)) {
                    res.add(y);
                    break;
                }
            }
        }

        if(res.size() > 0)
            return XQueryFilterValue.trueValue();
        return XQueryFilterValue.falseValue();
    }
}
