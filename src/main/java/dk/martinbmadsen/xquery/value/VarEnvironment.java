package dk.martinbmadsen.xquery.value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VarEnvironment implements IXQueryValue, Map<String, XQueryList> {
    private Map<String, XQueryList> varEnv = new HashMap<>();

    public VarEnvironment() {
    }

    public XQueryList getVar(String varName){
        XQueryList res = varEnv.get(varName);

        if(res != null)
            return res;
        return new XQueryList();
    }

    public VarEnvironment copy(){
        VarEnvironment ve = new VarEnvironment();
        ve.varEnv.putAll(this.varEnv);
        return ve;
    }

    @Override
    public int size() {
        return varEnv.size();
    }

    @Override
    public boolean isEmpty() {
        return varEnv.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return varEnv.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return varEnv.containsValue(value);
    }

    @Override
    public XQueryList get(Object key) {
        return varEnv.get(key);
    }

    @Override
    public XQueryList put(String key, XQueryList value) {
        return varEnv.put(key, value);
    }

    @Override
    public XQueryList remove(Object key) {
        return varEnv.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends XQueryList> m) {
        varEnv.putAll(m);
    }

    @Override
    public void clear() {
        varEnv.clear();
    }

    @Override
    public Set<String> keySet() {
        return varEnv.keySet();
    }

    @Override
    public Collection<XQueryList> values() {
        return varEnv.values();
    }

    @Override
    public Set<Entry<String, XQueryList>> entrySet() {
        return varEnv.entrySet();
    }
}
