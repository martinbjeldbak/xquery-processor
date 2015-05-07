package dk.martinbmadsen.xquery.value;

import java.util.HashMap;
import java.util.Map;

public class VarEnvironment implements IXQueryValue {
    public Map<String, XQueryList> varEnvs = new HashMap<>();

    public VarEnvironment() {
    }

    public XQueryList getVar(String varName){
        return varEnvs.get(varName);
    }

    public XQueryList putVar(String varName, XQueryList varValue){
        return varEnvs.put(varName, varValue);
    }
}
