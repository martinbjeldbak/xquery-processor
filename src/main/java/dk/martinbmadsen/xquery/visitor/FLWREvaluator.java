package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.ForClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.LetClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.ReturnClauseContext;
import dk.martinbmadsen.xquery.parser.XQueryParser.WhereClauseContext;
import dk.martinbmadsen.xquery.value.*;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FLWREvaluator extends XQueryEvaluator {
    public FLWREvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public VarEnvironmentList evalFor(@NotNull ForClauseContext ctx){
        VarEnvironmentList varEnvs = new VarEnvironmentList();
        varEnvs.addAll(getVarEnvs(0, ctx, qc.cloneVarEnv()));
        return varEnvs;
    }

    private VarEnvironmentList getVarEnvs (int var, @NotNull ForClauseContext ctx, VarEnvironment previousVE){
        VarEnvironmentList varEnvs = new VarEnvironmentList();
        XQueryList res = (XQueryList)visitor.visit(ctx.xq(var));
        if(var == ctx.xq().size() - 1){
            for(IXMLElement elem : res){
                VarEnvironment currentVE = new VarEnvironment();
                currentVE.putAll(previousVE);
                currentVE.put(ctx.Var(var).getText(), new XQueryList(elem));
                varEnvs.add(currentVE);
            }
            return varEnvs;
        }
        for (IXMLElement elem : res){
            qc.pushContextElement(elem);
            VarEnvironment currentVE = previousVE.copy();
            currentVE.put(ctx.Var(var).getText(), new XQueryList(elem));
            qc.pushVarEnv(currentVE);
            varEnvs.addAll(getVarEnvs(var + 1, ctx, currentVE));
            qc.popVarEnv();
            qc.popContextElement();
        }
        return varEnvs;
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
        return (XQueryFilter)visitor.visit(ctx.cond());
    }

    public XQueryList evalReturn(@NotNull ReturnClauseContext ctx) {
        return (XQueryList)visitor.visit(ctx.xq());
    }
}
