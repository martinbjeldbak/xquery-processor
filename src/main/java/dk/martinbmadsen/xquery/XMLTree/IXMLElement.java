package dk.martinbmadsen.xquery.XMLTree;

import java.util.List;

public interface IXMLElement {
    /**
     * Gets the parent of this element in a singleton list.
     * If there is no parent, then an empty list is returned.
     * @return the parent element. Empty list otherwise.
     */
    IXMLElement parent();

    /**
     * Gets a list of all of this element's children
     * @return the element's children
     */
    List<IXMLElement> children();

    /**
     * Gets the number of children this element has
     * @return the number of children this element has
     */
    Integer childrenCount();

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
