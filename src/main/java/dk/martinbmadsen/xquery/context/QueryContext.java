package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.value.VarEnvironment;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.util.Stack;

/**
 * This class keeps a context of node elements while visiting the XML tree and
 * evaluating different operators and queries.
 */
public class QueryContext {
    private Stack<XQueryListValue> ctxElems = new Stack<>();
    private Stack<VarEnvironment> varEnv = new Stack<>();

    public QueryContext() {
    }

    public XQueryListValue peekContextElement() {
        XQueryListValue res = new XQueryListValue(this.ctxElems.peek().size());
        for (IXMLElement x : this.ctxElems.peek())
            if (x != null)
                res.add(x);
        return res;
    }

    /**
     * Gets the current context element (WARNING: this pops it from the stack)
     * @return the {@link IXMLElement} we are currently exploring
     */
    public XQueryListValue popContextElement() {
        return this.ctxElems.pop();
    }

    /**
     * Pushes an element/tree onto the context stack.
     * @param elem the tree/element to be added as context
     */
    public void pushContextElement(IXMLElement elem) {
        this.ctxElems.push(new XQueryListValue(elem));
    }

    public void pushContextElement(XQueryListValue elem) {
        ctxElems.push(elem);
    }

    public XQueryListValue getVar(String varName) {
        if (varEnv.size() == 0)
            return null;
        return varEnv.peek().getVar(varName);
    }

    public XQueryListValue putVar(String varName, XQueryListValue varValue) {
        return varEnv.peek().putVar(varName, varValue);
    }

    public void popVarEnv(){
        varEnv.pop();
    }

    public void pushVarEnv(VarEnvironment ve){
        varEnv.push(ve);
    }
}
