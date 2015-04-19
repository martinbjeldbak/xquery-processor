package dk.martinbmadsen.xquery.XMLTree;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XMLElement implements IXMLElement {
    private Element elem;
    
    public XMLElement(Element element) {
        elem = element;
    }

    @Override
    public List<IXMLElement> parent() {
        List<IXMLElement> parent = new ArrayList<>(1);
        parent.add(new XMLElement(elem.getParentElement()));
        return parent;
    }

    @Override
    public List<IXMLElement> children() {
        return elem.getChildren().stream().map(
                XMLElement::new).collect(Collectors.toList());
    }

    @Override
    public Integer childrenCount() {
        return elem.getChildren().size();
    }

    @Override
    public String tag() {
        return elem.getName();
    }

    @Override
    public String txt() {
        return elem.getText();
    }

    @Override
    public String toString() {
        return elem.toString();
    }
}

