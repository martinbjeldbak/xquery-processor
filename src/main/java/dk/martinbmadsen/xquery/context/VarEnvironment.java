package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.value.XQueryList;

import java.util.HashMap;
import java.util.Map;

public class VarEnvironment {
    public Map<String, XQueryList> varEnvs = new HashMap<>();
    private VarEnvironment parent;

    public VarEnvironment() {
    }

    public VarEnvironment(VarEnvironment parent) {
        this.parent = parent;
    }

    public XQueryList getVar(String varName){
        XQueryList v = varEnvs.get(varName);

        if(v == null && parent != null)
            v = parent.getVar(varName);
        return v;
    }

    public XQueryList putVar(String varName, XQueryList varValue){
        return varEnvs.put(varName, varValue);
    }
}
