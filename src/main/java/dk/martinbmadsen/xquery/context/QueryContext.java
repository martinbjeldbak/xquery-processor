package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.value.VarEnvironment;
import dk.martinbmadsen.xquery.value.XQueryList;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.util.Stack;

/**
 * This class keeps a context of node elements while visiting the XML tree and
 * evaluating different operators and queries.
 */
public class QueryContext {
    private Stack<XQueryList> ctxElems = new Stack<>();
    private Stack<VarEnvironment> varEnv = new Stack<>();

    public QueryContext() {
    }

    public XQueryList peekContextElement() {
        XQueryList res = new XQueryList(this.ctxElems.peek().size());
        for (IXMLElement x : this.ctxElems.peek())
            if (x != null)
                res.add(x);
        return res;
    }

    /**
     * Gets the current context element (WARNING: this pops it from the stack)
     * @return the {@link IXMLElement} we are currently exploring
     */
    public XQueryList popContextElement() {
        return this.ctxElems.pop();
    }

    /**
     * Pushes an element/tree onto the context stack.
     * @param elem the tree/element to be added as context
     */
    public void pushContextElement(IXMLElement elem) {
        this.ctxElems.push(new XQueryList(elem));
    }

    public void pushContextElement(XQueryList elem) {
        ctxElems.push(elem);
    }

    public XQueryList getVar(String varName) {
        if (varEnv.size() == 0)
            return null;
        return varEnv.peek().getVar(varName);
    }

    public XQueryList putVar(String varName, XQueryList varValue) {
        return varEnv.peek().putVar(varName, varValue);
    }

    public void popVarEnv(){
        varEnv.pop();
    }

    public void pushVarEnv(VarEnvironment ve){
        varEnv.push(ve);
    }
}
