package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;

import java.util.Stack;

/**
 * Created by martin on 19/04/15.
 */
public class QueryContext {
    public Stack<IXMLElement> ctxElems = new Stack<>();

    public QueryContext() {

    }

    /**
     * Gets the current context element (WARNING: this pops it from the stack)
     * @return the {@link IXMLElement} we are currently exploring
     */
    public IXMLElement getContextElement() {
        return this.ctxElems.pop();
    }

    /**
     * Pushes an element/tree onto the context stack.
     * @param elem the tree/element to be added as context
     */
    public void addContextElement(IXMLElement elem) {
        this.ctxElems.push(elem);
    }
}
