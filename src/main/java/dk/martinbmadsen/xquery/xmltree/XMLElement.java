package dk.martinbmadsen.xquery.xmltree;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.List;
import java.util.stream.Collectors;

public class XMLElement implements IXMLElement {
    private Element elem;
    
    public XMLElement(Element element) {
        elem = element;
    }

    @Override
    public IXMLElement parent() {
        return new XMLElement(elem.getParentElement());
    }

    @Override
    public List<IXMLElement> children() {
        return elem.getChildren().stream().map(
                XMLElement::new).collect(Collectors.toList());
    }

    @Override
    public String tag() {
        return elem.getName();
    }

    @Override
    public IXMLElement txt() {
        // TODO: This should not return an {@link IXMLElement}, but probably an {@link IXQueryValue} of type Text, or something
        Element textEl = new Element("text");
        textEl.addContent(new Text(elem.getText()));
        return new XMLElement(textEl);
    }

    @Override
    public IXMLElement attrib(String attName) {
        // TODO: This should not return an {@link IXMLElement}, but probably an {@link IXQueryValue} of type Text, or something
        Attribute att = elem.getAttribute(attName);
        if (att == null)
            return null;

        Element attEl = new Element(attName);
        attEl.addContent(new Text(att.getValue()));

        return new XMLElement(attEl);
    }

    @Override
    public String toString() {
        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        return xout.outputString(elem);
    }

    @Override
    public String getValue(){
        return this.elem.getValue();
    }

    @Override
    public boolean equalsRef(IXMLElement o) {
        if(o instanceof XMLElement) {
            XMLElement e = (XMLElement)o;
            return elem.equals(e.elem);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof XMLElement) {
            // TODO: This is hacky... converting each to string form, then comparing them
            XMLElement e = (XMLElement)o;

            XMLOutputter xout = new XMLOutputter();
            String elemString  = xout.outputString(elem);
            String otherString = xout.outputString(e.elem);

            return elemString.equals(otherString);
        }
        return false;
    }
}

