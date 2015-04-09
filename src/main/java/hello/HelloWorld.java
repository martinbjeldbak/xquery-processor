package hello;

import java.io.File;
import java.util.List;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * Created by martin on 07/04/15.
 */
public class HelloWorld {
    public static void main(String[] args) {
        Greeter greeter = new Greeter();
        SAXBuilder builder = new SAXBuilder();

        System.out.println(greeter.sayHello());

        File caesarF = new File("samples/j_caesar.xml");

        try {
            Document document = builder.build(caesarF);
            Element rootNode = document.getRootElement();


            System.out.println(rootNode.getChildText("TITLE"));



        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
