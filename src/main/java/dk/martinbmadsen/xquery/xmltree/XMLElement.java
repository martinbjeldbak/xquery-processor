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
        Element textEl = new Element("text");
        textEl.addContent(new Text(elem.getText()));
        return new XMLElement(textEl);
    }

    @Override
    public IXMLElement attrib(String attName) {
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

    public Element getElement(){
        return this.elem;
    }

    public String getValue(){
        return this.elem.getValue();
    }

    public boolean equalsRef(IXMLElement o) {
        if(o instanceof XMLElement) {
            XMLElement e = (XMLElement)o;
            return elem.equals(e.getElement());
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof XMLElement) {
            return this.getValue().equals(((XMLElement) o).getValue());
            // XMLElement e = (XMLElement) o;

            // // Compare attributes
            // for (Attribute a : elem.getAttributes()) {
            //     for (Attribute b : e.getElement().getAttributes()) {
            //         if (!a.getName().equals(b.getName()) ||
            //                 !a.getValue().equals(b.getValue())) {
            //             return false;
            //         }
            //     }
            // }

            // // Compare text
            // if (!elem.getTextNormalize().equals(e.getElement().getTextNormalize()))
            //     return false;

            // // Compare children
            // for (Element a : elem.getChildren()) {
            //     for(Element b : elem.getChildren()) {
            //         XMLElement c1 = new XMLElement(a);
            //         XMLElement c2 = new XMLElement(b);

            //         if(!c1.equals(c2))
            //             return false;
            //     }
            // }

            // // If still here, return true
            // return true;
        }
        return false;
    }

}

