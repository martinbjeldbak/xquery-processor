package dk.martinbmadsen.xquery.debug;

public class Debug {
    public static void debug(String msg) {
        print("DEBUG: " + msg);
    }

    public static void result(String msg) {
        print("RESULT: " + msg);
    }

    private static void print(String msg) {
        System.out.println(msg);
    }
}
