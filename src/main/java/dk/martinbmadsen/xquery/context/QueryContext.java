package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.value.XQueryList;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;


/**
 * Encapsulates everything that has to do with the context of a query: from
 * handling where the engine is in the XML tree, to what XQuery variables are in scope.
 */
public class QueryContext {
    private SymbolTable st = new SymbolTable();
    private NodeContext nc = new NodeContext();

    public QueryContext() {
    }

    public XQueryList peekContextElement() {
        return nc.peekContextElement();
    }

    public XQueryList popContextElement() {
        return nc.popContextElement();
    }

    public void pushContextElement(IXMLElement elem) {
        nc.pushContextElement(elem);
    }

    public void pushContextElement(XQueryList elem) {
        nc.pushContextElement(elem);
    }

    public XQueryList getVar(String var) {
        return st.getVar(var);
    }

    public VarEnvironment openScope() {
        return st.openScope();
    }

    public VarEnvironment closeScope() {
        return st.closeScope();
    }
}
