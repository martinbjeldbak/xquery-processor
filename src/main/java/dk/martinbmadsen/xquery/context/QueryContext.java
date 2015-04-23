package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.util.List;
import java.util.Stack;

/**
 * Created by martin on 19/04/15.
 */
public class QueryContext {
    public Stack<IXMLElement> ctxElems = new Stack<>();

    public QueryContext() {

    }


    public IXMLElement peekContextElement() {
        return this.ctxElems.peek();
    }

    /**
     * Gets the current context element (WARNING: this pops it from the stack)
     * @return the {@link IXMLElement} we are currently exploring
     */
    public IXMLElement popContextElement() {
        return this.ctxElems.pop();
    }

    /**
     * Pushes an element/tree onto the context stack.
     * @param elem the tree/element to be added as context
     */
    public void pushContextElement(IXMLElement elem) {
        this.ctxElems.push(elem);
    }

    /**
     * Pushes each element in the list as a context element
     * @param elems list of element nodes to be added as context
     */
    public void pushContextElements(List<IXMLElement> elems) {
        elems.forEach(this::pushContextElement);
    }
}
