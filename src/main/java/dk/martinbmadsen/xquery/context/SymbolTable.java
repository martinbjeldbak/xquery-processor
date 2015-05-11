package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.value.XQueryList;

import java.util.EmptyStackException;
import java.util.Stack;

public class SymbolTable {
    private Stack<VarEnvironment> varEnv = new Stack<>();

    public SymbolTable() {

    }

    public XQueryList getVar(String varName) {
        return currentScope().getVar(varName);
    }

    public XQueryList putVar(String varName, XQueryList varValue) {
        return varEnv.peek().putVar(varName, varValue);
    }

    public VarEnvironment currentScope() {
        try {
            return varEnv.peek();
        }
        catch(EmptyStackException ex) {
            return null;
        }
    }

    /**
     * Opens a new scope, with this scope as its parent
     * @return the newly created scope
     */
    public VarEnvironment openScope() {
        return varEnv.push(new VarEnvironment(currentScope()));
    }

    public VarEnvironment closeScope() {
        return varEnv.pop();
    }

    public VarEnvironment closeScope(int levels) {
        VarEnvironment last = null;
        for(int i = 0; i < levels; i++)
            last = varEnv.pop();
        return last;
    }
}
