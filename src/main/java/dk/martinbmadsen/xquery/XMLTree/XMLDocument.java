package dk.martinbmadsen.xquery.XMLTree;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

import java.io.IOException;

public class XMLDocument implements IXMLDocument {
    private Document doc;

    public XMLDocument(String fileName) {
        SAXBuilder sax = new SAXBuilder(XMLReaders.DTDVALIDATING);

        try {
            this.doc = sax.build(sanitizeFileName(fileName));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replace("\"", "");
    }

    @Override
    public IXMLElement root() {
        return new XMLElement(doc.getRootElement());
    }
}
