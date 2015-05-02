package dk.martinbmadsen.utils.debug;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLDocument;

import java.util.*;

/**
 * <p>Produces values for theory parameters of type {@link String}.</p>
 *
 * <p>This implementation produces strings whose characters are in the interval [0x0000, 0xD7FF].</p>
 *
 * <p>The generated values will have {@linkplain String#length()} decided by
 * {@link com.pholser.junit.quickcheck.generator.GenerationStatus#size()}.</p>
 */
public class QueryGenerator extends Generator<String> {
    List<String> rps = new ArrayList<>();
    List<String> seperators = new ArrayList<>();
    List<String> attributes = new ArrayList<>();
    int maxsize = 20;
    int currentSize = 0;
    public QueryGenerator() {
        super(String.class);
        XMLDocument document = new XMLDocument("samples/xml/j_caesar.xml");

        // RPs
        for (IXMLElement x : document.root().descendants())
            rps.add(x.tag());
        rps = new ArrayList<String>(new HashSet<String>(rps)); // Remove duplicates

        // Seperators
        seperators.addAll(Arrays.asList("/", "//"));

        // Attributes
        for (IXMLElement x : document.root().descendants())
            attributes.addAll(x.getAttribNames());
        attributes = new ArrayList<String>(new HashSet<String>(attributes)); //Remove duplicates
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        currentSize = 0;
        // Don't allow comma as first seperator
        String seperator = seperators.get(random.nextInt(seperators.size()));
        return seperator + generateRP(random);
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