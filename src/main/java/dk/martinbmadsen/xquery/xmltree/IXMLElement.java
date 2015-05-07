package dk.martinbmadsen.xquery.xmltree;

import dk.martinbmadsen.xquery.value.XQueryList;
import org.jdom2.Element;

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
     * @return the parent element (of type {@link XMLElement} in a singleton {@link XQueryList}.
     * An empty list if this element is the root.
     */
    XQueryList parent();

    /**
     * Gets a list of all of this element's children. Returns
     * an empty list if the element has no children.
     * @return the element's children, a list of {@link IXMLElement}s
     * TODO: Make this return {@link XQueryList}
     */
    XQueryList children();

    /**
     * Gets text element associated to this element
     * @return the text element associated to this element
     */
    XMLText txt();

    /**
     * Returns the attribute value associated with the attribute key given
     * as input for this element
     * @param attName the attribute key with the structure <attName>attKey</attName>
     */
    IXMLElement attrib(String attName);

    /**
     * Gets a list of all the descendants of this element, not including the element itself
     * @return all of this element's descendants
     */
    XQueryList descendants();

    /**
     * Gets the tag of this element
     * @return the XML tag of this element
     */
    String tag();

    /**
     * @deprecated Because this method is an extension of {@link Element}, which only returns text() parts of
     * this {@link IXMLElement} and all its descendants, ignoring attributes.
     * Replaced by {@link #equals(Object)} or {@link #toString()}.
     * @return A string of this element's text along with all its descendents text nodes concatenated into one string
     */
    @Deprecated
    String getValue();

    List<String> getAttribNames();

    /**
     * Compares by reference.
     * @param o the other object to compare, an instantiated {@link IXMLElement}
     * @return  true if they point to the same tree
     */
    boolean equalsRef(IXMLElement o);

    /**
     * Compares objects by value.
     * @param o the other object to compare
     * @return true if they have the same content
     */
    boolean equals(Object o);
}
