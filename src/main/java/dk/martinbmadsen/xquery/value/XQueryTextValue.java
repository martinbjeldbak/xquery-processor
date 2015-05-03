package dk.martinbmadsen.xquery.value;

public class XQueryTextValue implements IXQueryValue {
    String value;

    public XQueryTextValue(String val) {
        this.value = val;
    }
    public String getValue(){
        return value;
    }
}
