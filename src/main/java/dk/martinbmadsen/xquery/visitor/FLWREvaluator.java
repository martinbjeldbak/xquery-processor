package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.ForClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.LetClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.ReturnClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.WhereClauseContext;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryList;
import org.antlr.v4.runtime.misc.NotNull;

public class FLWREvaluator extends XQueryEvaluator {
    public FLWREvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryList evalFor (@NotNull ForClauseContext ctx){
        return null;
    }

    public XQueryList evalLet (@NotNull LetClauseContext ctx){
        return null;
    }

    public XQueryList evalWhere (@NotNull WhereClauseContext ctx){
        return null;
    }

    public XQueryList evalReturn (@NotNull ReturnClauseContext ctx){
        return null;
    }
}
