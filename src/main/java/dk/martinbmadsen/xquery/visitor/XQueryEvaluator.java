package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.context.VarEnvironment;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.value.IXQueryValue;

public class XQueryEvaluator {
    protected XQueryBaseVisitor<IXQueryValue> visitor;
    protected QueryContext qc;
    protected VarEnvironment ve;

    protected XQueryEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        this.visitor = visitor;
        this.qc = qc;
    }
}
