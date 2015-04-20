package dk.martinbmadsen.xquery.XMLTree;

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
    public int childrenCount() {
        return elem.getChildren().size();
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
    public String toString() {
        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        return xout.outputString(elem);
    }
}

