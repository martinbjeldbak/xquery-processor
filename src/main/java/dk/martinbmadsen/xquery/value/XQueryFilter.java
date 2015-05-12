package dk.martinbmadsen.xquery.value;

/**
 * This class is simply a wrapper of boolean values returned by filters in
 * our XPath/XQuery implementation. It supports simple binary operations and
 * comparisons.
 */
public class XQueryFilter implements IXQueryValue {
    private static XQueryFilter trueVal = new XQueryFilter();
    private static XQueryFilter falseVal = new XQueryFilter();

    private XQueryFilter() {
    }

    public static XQueryFilter trueValue() {
        return trueVal;
    }

    public static XQueryFilter falseValue() {
        return falseVal;
    }

    public XQueryFilter not() {
        if(this == trueVal)
            return falseVal;
        return trueVal;
    }

    public XQueryFilter and(XQueryFilter other) {
        if(this == trueVal && other == trueVal)
            return trueVal;
        return falseVal;
    }

    public XQueryFilter or(XQueryFilter other) {
        if(this == trueVal || other == trueVal)
            return trueVal;
        return falseVal;
    }

    @Override
    public String toString() {
        if(this == trueVal)
            return "XQueryFilter True";
        else
            return "XQueryFilter False";
    }
}
