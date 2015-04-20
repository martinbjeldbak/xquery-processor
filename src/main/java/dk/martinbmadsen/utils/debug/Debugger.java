package dk.martinbmadsen.utils.debug;

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
}
