package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import org.antlr.v4.runtime.misc.NotNull;

public class FLWREvaluator extends XQueryEvaluator {
    public FLWREvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryListValue evalFor (@NotNull ForClauseContext ctx){
        return null;
    }

    public XQueryListValue evalLet (@NotNull LetClauseContext ctx){
        return null;
    }

    public XQueryListValue evalWhere (@NotNull WhereClauseContext ctx){
        return null;
    }

    public XQueryListValue evalReturn (@NotNull ReturnClauseContext ctx){
        return null;
    }
}
