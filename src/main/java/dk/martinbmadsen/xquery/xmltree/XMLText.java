package dk.martinbmadsen.xquery.xmltree;

import dk.martinbmadsen.xquery.value.XQueryListValue;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.ArrayList;
import java.util.List;

public class XMLText implements IXMLElement {
    String value;
    Element parent;

    public XMLText(String val){
        this.value = val;
    }

    public XMLText(Element elem){
        this.parent = elem;
        this.value = elem.getText();
    }

    @Override
    public IXMLElement parent() {
        return new XMLElement(parent);
    }

    @Override
    public XQueryListValue children() {
        return new XQueryListValue();
    }

    @Override
    public IXMLElement txt() {
        return null;
    }

    @Override
    public IXMLElement attrib(String attName) {
        return null;
    }

    @Override
    public XQueryListValue descendants() {
        return new XQueryListValue();
    }

    @Override
    public String tag() {
        return null;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public List<String> getAttribNames() {
        return new ArrayList<>();
    }

    @Override
    public boolean equalsRef(IXMLElement o) {
        if(o instanceof XMLText) {
            XMLText e = (XMLText)o;
            return this == e;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof XMLText) {
            XMLText e = (XMLText)o;
            return value.equals(e.getValue());
        }
        return false;
    }
}
