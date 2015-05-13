package dk.martinbmadsen.xquery.xmltree;

import dk.martinbmadsen.xquery.value.XQueryList;
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

    public XMLElement(String tagName) {
        elem = new Element(tagName);
    }

    public void add(XMLElement child) {
        elem.addContent(child.elem.clone());
    }

    public void add(XMLText txt) {
        elem.addContent(txt.toString());
    }

    @Override
    public XQueryList parent() {
        XQueryList res = new XQueryList(1);

        Element parentEl = elem.getParentElement();

        if(parentEl != null)
            res.add(new XMLElement(parentEl));
        return res;
    }

    @Override
    public XQueryList children() {
        return this.elem.getChildren().stream().map(
                XMLElement::new).collect(Collectors.toCollection(XQueryList::new));
    }

    @Override
    public String tag() {
        return elem.getName();
    }

    @Override
    public XMLText txt() {
        return new XMLText(elem);
    }

    @Override
    public IXMLElement attrib(String attName) {
        // TODO: This should not return an {@link IXMLElement}, but probably an {@link IXQueryValue} of type Text, or something
        // TODO: This is the root of all evil (returning null!)
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
    public boolean equalsRef(IXMLElement o) {
        if(o instanceof XMLElement) {
            XMLElement e = (XMLElement)o;
            return elem.equals(e.elem);
        }
        return false;
    }

    @Override
    public List<String> getAttribNames(){
        return this.elem.getAttributes().stream().map(
                Attribute::getName).collect(Collectors.toList());
    }

    @Override
    public XQueryList descendants() {
        XQueryList res = new XQueryList();
        res.add(this);

        for (IXMLElement e : children()){
            res.add(e);
            res.addAll(e.descendants());
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof XMLElement) {
            // TODO: This is hacky... converting each to string form, then comparing them
            XMLElement e = (XMLElement)o;

            XMLOutputter xout = new XMLOutputter(Format.getCompactFormat());
            String elemString  = xout.outputString(elem);
            String otherString = xout.outputString(e.elem);

            return elemString.equals(otherString);
        }
        return false;
    }

    @Override
    public String toCompactString() {
        return new XMLOutputter(Format.getCompactFormat()).outputString(elem);
    }
}

