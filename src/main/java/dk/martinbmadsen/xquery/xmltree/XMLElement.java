package dk.martinbmadsen.xquery.xmltree;

import dk.martinbmadsen.xquery.value.XQueryListValue;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.ArrayList;
import java.util.List;

public class XMLElement implements IXMLElement {
    private Element elem;
    
    public XMLElement(Element element) {
        elem = element;
    }

    public XMLElement(String tagName, XQueryListValue content) {
        elem = new Element(tagName);
        for (IXMLElement x : content)
            elem.addContent(x.toString());
    }

    @Override
    public XMLElement parent() {
        Element parentEl = elem.getParentElement();
        if(parentEl == null)
            return null;
        else
            return new XMLElement(parentEl);
    }

    @Override
    public XQueryListValue children() {
        XQueryListValue children = new XQueryListValue();
        for (Element elem : this.elem.getChildren())
            children.add(new XMLElement(elem));

        return children;
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
    public List<String> getAttribNames(){
        List<String> attribNames = new ArrayList<>();
        for (Attribute x : this.elem.getAttributes())
            attribNames.add(x.getName());
        return attribNames;
    }

    @Override
    public XQueryListValue descendants() {
        Iterable<Content> descendants = elem.getDescendants();
        XQueryListValue res = new XQueryListValue();

        res.add(new XMLElement(elem));

        for(Content c : descendants) {
            if(c instanceof Element) {
                Element e = (Element)c;

                res.add(new XMLElement(e));
            }
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
}

