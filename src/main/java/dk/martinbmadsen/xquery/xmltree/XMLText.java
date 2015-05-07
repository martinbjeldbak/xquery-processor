package dk.martinbmadsen.xquery.xmltree;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.value.XQueryList;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class XMLText implements IXMLElement {
    String value;
    Element parent;

    public XMLText(String val){
        if(val.charAt(0) == '"' && val.charAt(val.length()-1) == '"')
            this.value = val.substring(1, val.length() - 1);
        else
            this.value = val;
    }

    public XMLText(Element elem){
        this.parent = elem;
        this.value = elem.getText();
    }

    @Override
    public XQueryList parent() {
        return new XQueryList(new XMLElement(parent));
    }

    @Override
    public XQueryList children() {
        return new XQueryList();
    }

    @Override
    public XMLText txt() {
        Debugger.error("Called txt() on a text element.");
        return null;
    }

    @Override
    public IXMLElement attrib(String attName) {
        Debugger.error("Called attrib() on a text element, which cannot have attributes.");
        return null;
    }

    @Override
    public XQueryList descendants() {
        return new XQueryList(0);
    }

    @Override
    public String tag() {
        Debugger.error("Called attrib() on a text element, which doesn't have a tag.");
        return null;
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
            return value.equals(e.value);
        }
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}
