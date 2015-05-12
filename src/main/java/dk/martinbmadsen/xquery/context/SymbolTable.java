package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.value.VarEnvironment;
import dk.martinbmadsen.xquery.value.XQueryList;

import java.util.Stack;

public class SymbolTable {
    private Stack<VarEnvironment> varEnv = new Stack<>();

    public SymbolTable() {
        varEnv.push(new VarEnvironment());
    }

    public VarEnvironment pushVarEnv(VarEnvironment ve) {
        return varEnv.push(ve);
    }

    public XQueryList getVar(String var) {
        return varEnv.peek().getVar(var);
    }

    public VarEnvironment popVarEnv() {
        return varEnv.pop();
    }

    public VarEnvironment cloneVarEnv() {
        return varEnv.peek().copy();
    }
}
