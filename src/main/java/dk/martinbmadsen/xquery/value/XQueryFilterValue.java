package dk.martinbmadsen.xquery.value;

/**
 * This class is simply a wrapper of boolean values returned by filters in
 * our XPath/XQuery implementation. It supports simple binary operations and
 * comparisons.
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

    public XQueryFilterValue and(XQueryFilterValue other) {
        if(this == trueVal && other == trueVal)
            return trueVal;
        return falseVal;
    }

    public XQueryFilterValue or(XQueryFilterValue other) {
        if(this == trueVal || other == trueVal)
            return trueVal;
        return falseVal;
    }
}
