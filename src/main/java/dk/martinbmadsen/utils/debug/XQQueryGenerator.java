package dk.martinbmadsen.utils.debug;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLDocument;

import java.util.*;

public class XQQueryGenerator extends Generator<String> {
    List<String> stringContants = new ArrayList<>();
    List<String> rps = new ArrayList<>();
    List<String> seperators = new ArrayList<>();
    List<String> attributes = new ArrayList<>();
    List<String> varnames = new ArrayList<>();
    List<String> tagnames = new ArrayList<>();
    QueryGenerator rpGen;
    int maxsize = 5;
    int currentSize = 0;
    public XQQueryGenerator() {
        super(String.class);
        XMLDocument document = new XMLDocument("samples/xml/j_caesar.xml");

        //String constants
        for (IXMLElement x : document.root().descendants())
            stringContants.add(x.txt().toString());
        stringContants = new ArrayList<String>(new HashSet<String>(stringContants)); // Remove duplicates

        for (IXMLElement x : document.root().descendants())
            rps.add(x.tag());
        rps = new ArrayList<String>(new HashSet<String>(rps)); // Remove duplicates

        // Seperators
        seperators.addAll(Arrays.asList("/", "//"));

        // Attributes
        for (IXMLElement x : document.root().descendants())
            attributes.addAll(x.getAttribNames());
        attributes = new ArrayList<String>(new HashSet<String>(attributes)); //Remove duplicates

        for(char c : "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
            varnames.add("$" + c);
            tagnames.add(""+c);
        }
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        currentSize = 0;
        return generateXQ(random);
    }

    private String generateXQ(SourceOfRandomness random) {
        if (currentSize++ >= maxsize)
            return varnames.get(random.nextInt(varnames.size()));
        int ranNum = random.nextInt(100);
        //chance is equal to chance of specific event happening
        int chance = 0;
        if (ranNum < (chance += 5))
            return varnames.get(random.nextInt(varnames.size()));
        else if (ranNum < (chance += 5))
            return "\"" + stringContants.get(random.nextInt(stringContants.size())) + "\"";
        else if (ranNum < (chance += 20))
            return "doc(\"samples/xml/j_caesar.xml\")" + seperators.get(random.nextInt(seperators.size())) + generateRP(random);
        else if (ranNum < (chance += 5))
            return '(' + generateXQ(random) + ')';
        else if (ranNum < (chance += 20))
            return generateXQ(random) + "/" + generateRP(random);
        else if (ranNum < (chance += 5))
            return generateXQ(random) + "//" + generateRP(random);
        else if (ranNum < (chance += 5))
            return generateXQ(random) + "," + generateXQ(random);
        else if (ranNum < (chance += 5)){
            String tagname = tagnames.get(random.nextInt(tagnames.size()));
            return '<' + tagname + ">{" +  generateXQ(random) + "}</" + tagname + ">";
        }
        else if (ranNum < (chance += 15))
            return generateFor(random) + generateLet(random) + generateWhere(random) + generateReturn(random);
        else return generateLet(random) + " " + generateXQ(random);
    }

    private String generateFor(SourceOfRandomness random){
        String res = " for " + varnames.get(random.nextInt(varnames.size())) +
                     " in " + generateXQ(random);
        int ranNum = random.nextInt(5);
        for(int i = 0; i < ranNum; i++)
            res += ", " + varnames.get(random.nextInt(varnames.size())) + " in " + generateXQ(random);
        return res;
    }

    private String generateLet(SourceOfRandomness random){
        String res = " let " + varnames.get(random.nextInt(varnames.size())) +
                " := " + generateXQ(random);
        int ranNum = random.nextInt(5);
        for(int i = 0; i < ranNum; i++)
            res += ", " + varnames.get(random.nextInt(varnames.size())) + " := " + generateXQ(random);
        return res;
    }

    private String generateWhere(SourceOfRandomness random){
        return " where " + generateCond(random);
    }

    private String generateReturn(SourceOfRandomness random){
        return " return " + generateXQ(random);
    }

    private String generateCond(SourceOfRandomness random){
        int ranNum = random.nextInt(100);
        //chance is equal to chance of specific event happening
        int chance = 0;
        if (ranNum < (chance += 15))
            return generateXQ(random) + " = " + generateXQ(random);
        else if (ranNum < (chance += 15))
            return generateXQ(random) + " == " + generateXQ(random);
        else if (ranNum < (chance += 10))
            return generateXQ(random) + " is " + generateXQ(random);
        else if (ranNum < (chance += 10))
            return generateXQ(random) + " eq " + generateXQ(random);
        else if (ranNum < (chance += 10))
            return generateCond(random) + " and " + generateCond(random);
        else if (ranNum < (chance += 5))
            return " (" + generateCond(random) + ")";
        else if (ranNum < (chance += 5))
            return " empty(" + generateXQ(random) + ")";
        else if (ranNum < (chance += 5)){
            String res = " some " + varnames.get(random.nextInt(varnames.size())) +
                    " in " + generateXQ(random);
            for(int i = 0; i < random.nextInt(5); i++)
                res += ", " + varnames.get(random.nextInt(varnames.size())) + " in " + generateXQ(random);
            res += " satisfies " + generateCond(random);
            return res;
        }
        else if (ranNum < (chance += 5))
            return generateCond(random) + " or " + generateCond(random);
        else return "not " + generateCond(random);
    }

    private String generateRP(SourceOfRandomness random) {
        if (currentSize++ >= maxsize)
            return rps.get(random.nextInt(rps.size()));
        int ranNum = random.nextInt(100);
        //chance is equal to chance of specific event happening
        int chance = 0;
        if (ranNum < (chance += 5))
            return "*";
        else if (ranNum < (chance += 5))
            return ".";
        else if (ranNum < (chance += 5))
            return "..";
        else if (ranNum < (chance += 5))
            return "text()";
        else if (ranNum < (chance += 5))
            return '(' + generateRP(random) + ')';
        else if (ranNum < (chance += 30))
            return generateRP(random) + "/" + generateRP(random);
        else if (ranNum < (chance += 5))
            return generateRP(random) + "//" + generateRP(random);
        else if (ranNum < (chance += 5))
            return generateRP(random) + '[' + generateFilter(random) + ']';
        else if (ranNum < (chance += 5))
            return generateRP(random) + "," + generateRP(random);
        else if (ranNum < (chance += 5) && attributes.size() > 0)
            return "@" + attributes.get(random.nextInt(attributes.size()));
        else return rps.get(random.nextInt(rps.size()));
    }

    private String generateFilter(SourceOfRandomness random){
        if (currentSize++ >= maxsize)
            return rps.get(random.nextInt(rps.size()));
        int ranNum = random.nextInt(100);
        //chance is equal to chance of specific event happening
        int chance = 0;
        if (ranNum < (chance += 15))
            return generateRP(random);
        else if (ranNum < (chance += 15))
            return generateRP(random) + " = " + generateRP(random);
        else if (ranNum < (chance += 15))
            return generateRP(random) + " == " + generateRP(random);
        else if (ranNum < (chance += 15))
            return generateRP(random) + " is " + generateRP(random);
        else if (ranNum < (chance += 15))
            return generateRP(random) + " eq " + generateRP(random);
        else if (ranNum < (chance += 5))
            return generateFilter(random) + " and " + generateFilter(random);
        else if (ranNum < (chance += 5))
            return generateFilter(random) + " or " + generateFilter(random);
        else return "not " + generateFilter(random);
    }
}