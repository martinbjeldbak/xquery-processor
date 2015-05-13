package dk.martinbmadsen.xquery.xmltree;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;

public class XMLDocument implements IXMLDocument {
    private Document doc;

    public XMLDocument(String fileName) {
        //SAXBuilder sax = new SAXBuilder(XMLReaders.DTDVALIDATING);
        SAXBuilder sax = new SAXBuilder();

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
    public XMLElement root() {
        return new XMLElement(doc.getRootElement());
    }

}
