package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.value.XQueryListValue;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class VarEnvironment {
    public Map<String, XQueryListValue> varEnvs = new HashMap<>();

    public VarEnvironment() {
    }

    public XQueryListValue getVar(String varName){
        return varEnvs.get(varName);
    }

    public XQueryListValue putVar(String varName, XQueryListValue varValue){
        return varEnvs.put(varName, varValue);
    }
}
