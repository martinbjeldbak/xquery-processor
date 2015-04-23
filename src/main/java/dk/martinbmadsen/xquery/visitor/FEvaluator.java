package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilterValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;

import static dk.martinbmadsen.xquery.parser.XQueryParser.FContext;
import static dk.martinbmadsen.xquery.parser.XQueryParser.FRpContext;

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
}
