package dk.martinbmadsen.xquery.XMLTree;

import java.util.List;

/**
 * Created by martin on 18/04/15.
 */
public interface IXMLElement {
    /**
     * Gets the parent of this element
     * @return the parent element
     */
    IXMLElement parent();

    /**
     * Gets a list of all of this element's children
     * @return the element's children
     */
    List<IXMLElement> children();

    /**
     * Gets the tag of this element
     * @return the XML tag of this element
     */
    String tag();
}
