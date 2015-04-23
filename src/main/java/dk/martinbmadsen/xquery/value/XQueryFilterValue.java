package dk.martinbmadsen.xquery.value;

/**
 * Created by martin on 23/04/15.
 */
public class XQueryFilterValue implements IXQueryValue {
    private static XQueryFilterValue trueVal = new XQueryFilterValue();
    private static XQueryFilterValue falseVal = new XQueryFilterValue();

    private XQueryFilterValue() {
    }

    public static XQueryFilterValue trueValue() {
        return trueVal;
    }

    public static XQueryFilterValue falseValue() {
        return falseVal;
    }

    public XQueryFilterValue not() {
        if(this == trueVal)
            return falseVal;
        return trueVal;
    }
}
