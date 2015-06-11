package dk.martinbmadsen.utils.debug;

import java.util.Map;

public class Debugger {
    public static void debug(String msg) {
        print("DEBUG: " + msg);
    }

    public static void result(String msg) {
        print("RESULT: " + msg);
    }

    public static void error(String msg) {
        System.err.println("ERROR: " + msg);
    }

    private static void print(String msg) {
        System.out.println(msg);
    }

    public static void printMap(Map<String, String> mapping) {
        for (String name: mapping.keySet()){
            String key = name;
            String value = mapping.get(name);
            System.out.println(key + ": " + value);
        }
    }
}
