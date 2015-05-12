package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.ForClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.LetClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.ReturnClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.WhereClauseContext;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.VarEnvironment;
import dk.martinbmadsen.xquery.value.XQueryList;
import org.antlr.v4.runtime.misc.NotNull;

public class FLWREvaluator extends XQueryEvaluator {
    public FLWREvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public VarEnvironment evalFor(@NotNull ForClauseContext ctx){
        VarEnvironment ve = new VarEnvironment();
        for(int i = 0; i < ctx.xq().size(); i++) {
            XQueryList res = (XQueryList)visitor.visit(ctx.xq(i));
            ve.putVar(ctx.Var(i).getText(), res);
        }
        return ve;
    }

    /**
     * Evaluates a let expression, thus updating the global scope
     * @param ctx list of xquery and variable expression
     */
    public VarEnvironment evalLet(@NotNull LetClauseContext ctx){
        VarEnvironment ve = qc.cloneVarEnv();

        for(int i = 0; i < ctx.xq().size(); i++) {
            XQueryList res = (XQueryList)visitor.visit(ctx.xq(i));
            ve.putVar(ctx.Var(i).getText(), res);
        }
        return ve;
    }

    public XQueryList evalWhere(@NotNull WhereClauseContext ctx){
        return null;
    }

    public XQueryList evalReturn(@NotNull ReturnClauseContext ctx){
        return null;
    }
}
