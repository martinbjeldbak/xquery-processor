package dk.martinbmadsen.xquery.main;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.utils.debug.XQueryExecutor;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("You must run this program with 1 parameter, being the file name");
            System.exit(-1);
        }

        List<IXMLElement> result = new ArrayList<>();
        try {
            result = XQueryExecutor.executeFromFile(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(args.length > 1 && args[1].equals("terminal")){
            System.out.println(result.size() + " results below:");
            Integer i = 0;
            for(IXMLElement c : result) {
                Debugger.result("#" + i++);
                System.out.println(c.toString());
            }
        }
        else{
            try{
                PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
                for(IXMLElement c : result) {
                    writer.println(c.toString());
                }
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
