package dk.martinbmadsen.xquery.XMLTree;

import java.util.List;

/**
 * Interface for serialized XML elements. All implementers of this fulfill the
 * same functionality as described in the XQuery Semantics not from the CSE 232B
 * homepage <a href="http://db.ucsd.edu/cse232b/notes/xpath-semantics.pdf">here</a>.
 *
 * This interface is to be passed around in the visitor class, as all required functionality
 * required to implement the semantics is defined by the below methods.
 *
 * {@link XMLElement} is an implementation of this interface. It wraps the
 * {@link org.jdom2.Element} object.
 */
public interface IXMLElement {
    /**
     * Gets the parent of this element in a singleton list.
     * If there is no parent, then an empty list is returned.
     * @return the parent element, (also an {@link IXMLElement}). Empty list otherwise.
     */
    IXMLElement parent();

    /**
     * Gets a list of all of this element's children
     * @return the element's children, a list of {@link IXMLElement}s
     */
    List<IXMLElement> children();

    /**
     * Gets the number of children this element has
     * @return the number of children this element has
     */
    int childrenCount();

    /**
     * Gets the tag of this element
     * @return the XML tag of this element
     */
    String tag();

    /**
     * Gets text node associated to this element
     * @return the text node associated to this element
     */
    String txt();
}
