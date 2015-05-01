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
    List<String> filters = new ArrayList<>();
    public QueryGenerator() {
        super(String.class);
        XMLDocument document = new XMLDocument("samples/xml/j_caesar.xml");

        // RPs
        for (IXMLElement x : document.root().descendants())
            rps.add(x.tag());
        rps.addAll(Arrays.asList("*", ".", "..", "text()"));
        rps = new ArrayList<String>(new HashSet<String>(rps));

        // Seperators
        seperators.addAll(Arrays.asList("/", "//", ","));

        //Filters
        for (int i = 0; i < rps.size(); i++){
            filters.add('[' + generateFilter() + ']');
        }

        int filterLen = filters.size();
        for (int i = 0; i < filterLen * 2; i++)
            filters.add(""); // Make filters optional
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        String query = "";
        int querySize = random.nextInt(10);
        for (int i = 0; i <= querySize; i++) {
            String seperator = seperators.get(random.nextInt(seperators.size()));
            if(i == 0 && seperator.equals(",")) // Don't allow comma as first seperator
                seperator = "/";
            String rp = rps.get(random.nextInt(rps.size()));
            String filter = filters.get(random.nextInt(filters.size()));
            query += seperator + rp + filter;
        }
        return query;
    }

    private String generateFilter(){
        Random r = new Random();
        switch (r.nextInt(8)){
            case 0:
                return rps.get(r.nextInt(rps.size()));
            case 1:
                return rps.get(r.nextInt(rps.size())) + " = " + rps.get(r.nextInt(rps.size()));
            case 2:
                return rps.get(r.nextInt(rps.size())) + " == " + rps.get(r.nextInt(rps.size()));
            case 3:
                return rps.get(r.nextInt(rps.size())) + " is " + rps.get(r.nextInt(rps.size()));
            case 4:
                return rps.get(r.nextInt(rps.size())) + " eq " + rps.get(r.nextInt(rps.size()));
            case 5:
                return generateFilter() + " and " + generateFilter();
            case 6:
                return generateFilter() + " or " + generateFilter();
            case 7:
                return "not " + generateFilter();
            default: return "";
        }
    }
}