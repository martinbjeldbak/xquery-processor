package dk.martinbmadsen.utils.debug;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SampleReader {
    public static String openQueryFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(String.format("samples/xquery/%s", fileName))));
    }
}
