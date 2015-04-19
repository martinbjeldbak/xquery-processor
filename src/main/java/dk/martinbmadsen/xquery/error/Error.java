package dk.martinbmadsen.xquery.error;


public abstract class Error extends Exception implements IError {
    public Error(String msg) {
        super(msg);
    }
}
