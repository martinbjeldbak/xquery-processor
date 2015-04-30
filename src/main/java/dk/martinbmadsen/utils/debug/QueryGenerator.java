package dk.martinbmadsen.utils.debug;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 * <p>Produces values for theory parameters of type {@link String}.</p>
 *
 * <p>This implementation produces strings whose characters are in the interval [0x0000, 0xD7FF].</p>
 *
 * <p>The generated values will have {@linkplain String#length()} decided by
 * {@link com.pholser.junit.quickcheck.generator.GenerationStatus#size()}.</p>
 */
public class QueryGenerator extends Generator<String> {
    public QueryGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness random, GenerationStatus status) {
        String query = "//TITLE";



        return query;
    }
}