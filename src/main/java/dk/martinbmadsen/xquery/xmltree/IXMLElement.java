package dk.martinbmadsen.xquery.xmltree;

import dk.martinbmadsen.utils.debug.QueryGenerator;
import dk.martinbmadsen.xquery.value.XQueryList;
import org.jdom2.Attribute;
import org.jdom2.output.Format;

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
     * Gets a list of all of this element's children with a specific tag.
     * Returns an empty list if the element has no children with that tagname.
     * @param tagName the tagname to find children of
     * @return the element's children with that tagName, a list of {@link IXMLElement}s
     */
    XQueryList getChildByTag(String tagName);

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
     * Used by {@link QueryGenerator#QueryGenerator()} to create elements
     * @return a list of each attribute, see {@link Attribute#getName()} for what this string is.
     */
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

    /**
     * Compares objects by children's values.
     * @param o the other object to compare
     * @return true if their children have the same content
     */
    boolean childrenEquals(Object o);

    /**
     * Converts this element to a one line string, including all attribs, text, and sub-elements.
     * @return a compact String representation of this element, see {@link Format#getCompactFormat()}
     */
    String toCompactString();

    /**
     * Converts this element to a pretty-printed string, including all attributes, text, and sub-elements.
     * @return a pretty-printed String representation of this element, see {@link Format#getPrettyFormat()}}
     */
    String toString();
}
