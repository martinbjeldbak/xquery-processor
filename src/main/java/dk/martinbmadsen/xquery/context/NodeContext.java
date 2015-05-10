package dk.martinbmadsen.xquery.context;

import dk.martinbmadsen.xquery.value.XQueryList;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.util.Stack;

/**
 * This class keeps a context of node elements while visiting the XML tree and
 * evaluating different operators and queries.
 */
public class NodeContext {
    private Stack<XQueryList> ctxElems = new Stack<>();

    public NodeContext() {

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
}
