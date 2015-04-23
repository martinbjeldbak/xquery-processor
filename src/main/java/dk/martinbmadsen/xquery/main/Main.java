package dk.martinbmadsen.xquery.main;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.executor.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<IXMLElement> result = null;
        try {
            result = XQueryExecutor.executeFromFile("samples/xquery/test.xq");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result.size() + " results below:");
            Integer i = 0;
            for(IXMLElement c : result) {
                Debugger.result("#" + i++);
                System.out.println(c.toString());
            }
    }

    public int giveTwo() {
        return 2;
    }



}
