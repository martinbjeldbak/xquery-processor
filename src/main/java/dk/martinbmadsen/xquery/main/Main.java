package dk.martinbmadsen.xquery.main;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.executor.XQueryExecutor;

import java.util.List;

public class Main {
    public static void main(String[] args) {
            /* Old: below stuff has now moved to tests
            List<IXMLElement> result = XQueryExecutor.executeFromFile("samples/xquery/test.xq");

            System.out.println(result.size() + " results below:");
            Integer i = 0;
            for(IXMLElement c : result) {
                Debugger.result("#" + i++);
                System.out.println(c.toString());
            }
            */
    }

    public int giveTwo() {
        return 2;
    }



}
