package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.ForClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.LetClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.ReturnClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.WhereClauseContext;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.VarEnvironment;
import dk.martinbmadsen.xquery.value.XQueryFilter;
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
            ve.put(ctx.Var(i).getText(), res);
            qc.pushVarEnv(ve);
        }
        ve = qc.cloneVarEnv();
        for(int i = 0; i < ctx.xq().size(); i++)
            qc.popVarEnv();
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
            ve.put(ctx.Var(i).getText(), res);
        }
        return ve;
    }

    public XQueryFilter evalWhere(@NotNull WhereClauseContext ctx){
        //XQueryList res = new XQueryList();
        //XQueryList xs = qc.peekContextElement();

        return (XQueryFilter)visitor.visit(ctx.cond());

        /*
        for(IXMLElement x : xs) {
            qc.pushContextElement(x);

            XQueryFilter y = (XQueryFilter)visitor.visit(ctx.cond());

            qc.popContextElement();

            if(y == XQueryFilter.trueValue())
                res.add(x);
        }
        return res;
        */
    }

    public XQueryList evalReturn(@NotNull ReturnClauseContext ctx) {
        return (XQueryList)visitor.visit(ctx.xq());
    }
}
