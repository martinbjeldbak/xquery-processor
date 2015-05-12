package dk.martinbmadsen.xquery.value;

import java.util.HashMap;
import java.util.Map;

public class VarEnvironment implements IXQueryValue {
    private Map<String, XQueryList> varEnv = new HashMap<>();

    public VarEnvironment() {
    }

    public XQueryList getVar(String varName){
        XQueryList res = varEnv.get(varName);

        if(res != null)
            return res;
        return new XQueryList();
    }

    public XQueryList putVar(String varName, XQueryList varValue){
        return varEnv.put(varName, varValue);
    }

    public VarEnvironment copy(){
        VarEnvironment ve = new VarEnvironment();
        ve.varEnv.putAll(this.varEnv);
        return ve;
    }
}
