package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilterValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLDocument;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.stream.Collectors;

public class ApRpEvaluator extends XQueryEvaluator {

    public ApRpEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryListValue evalAp(@NotNull ApContext ctx) {
        XMLDocument document = new XMLDocument(ctx.fileName.getText());
        qc.pushContextElement(document.root());
        XQueryListValue results = new XQueryListValue();

        switch(ctx.slash.getType()) {
            case XQueryParser.SLASH:
                results.addAll((XQueryListValue)visitor.visit(ctx.rp()));
                break;
            case XQueryParser.SSLASH:
                qc.pushContextElement(document.root().descendants());
                results.addAll(((XQueryListValue)visitor.visit(ctx.rp())).unique());
                break;
            default:
                Debugger.error("Oops, shouldn't be here");
                break;
        }
        return results;
    }

    public XQueryListValue evalTagName(@NotNull RpTagNameContext ctx) {
        String tagName = ctx.getText();

        return evalWildCard().stream().filter(
                e -> e.tag().equals(tagName)
        ).collect(Collectors.toCollection(XQueryListValue::new));
    }

    public XQueryListValue evalWildCard() {
        XQueryListValue res = new XQueryListValue();

        for(IXMLElement context : qc.peekContextElement()) {
            res.addAll(context.children());
        }
        return res;
    }

    public XQueryListValue evalDot() {
        return qc.peekContextElement();
    }

    public XQueryListValue evalDotDot() {
        XQueryListValue res = qc.peekContextElement().stream().map(
                IXMLElement::parent
        ).collect(Collectors.toCollection(XQueryListValue::new));
        return res.unique();
    }

    public XQueryListValue evalText() {
        XQueryListValue res = new XQueryListValue(qc.peekContextElement().size());

        for(IXMLElement x : qc.peekContextElement()) {
            res.add(x.txt());
        }

        return res;
    }

    public XQueryListValue evalAttr(RpAttrContext ctx) {
        // TODO: Don't return a list value here, return an attribute value... or something!
        String attrib = ctx.Identifier().getSymbol().getText();
        XQueryListValue res = new XQueryListValue(qc.peekContextElement().size());

        if(attrib == null)
            return null;

        for(IXMLElement x : qc.peekContextElement()) {
            if(x.attrib(attrib) == null)
                return null;
            res.add(x.attrib(attrib));
        }
        return res;
    }

    public XQueryListValue evalParen(@NotNull RpParenExprContext ctx) {
        return (XQueryListValue)visitor.visit(ctx.rp());
    }

    public XQueryListValue evalSlashes(@NotNull RpSlashContext ctx) {
        XQueryListValue results = new XQueryListValue();
        switch(ctx.slash.getType()) {
            case XQueryParser.SLASH:
                results = evalRpSlash(ctx);
                break;
            case XQueryParser.SSLASH:
                results = evalRpSlashSlash(ctx);
                break;
            default:
                Debugger.error("Oops, shouldn't be here");
                break;
        }
        return results;
    }

    public XQueryListValue evalRpSlash(@NotNull RpSlashContext ctx) {
        XQueryListValue y = new XQueryListValue();
        XQueryListValue x = (XQueryListValue)visitor.visit(ctx.left);

        qc.pushContextElement(x);

        XQueryListValue context = (XQueryListValue)visitor.visit(ctx.right);
        y.addAll(context);

        qc.popContextElement();
        return y.unique();
    }

    public XQueryListValue evalRpSlashSlash(@NotNull RpSlashContext ctx) {
        XQueryListValue l = (XQueryListValue)visitor.visit(ctx.left);
        XQueryListValue descendants = new XQueryListValue();

        for(IXMLElement x : l)
            descendants.addAll(x.descendants());

        qc.pushContextElement(descendants);

        XQueryListValue r = (XQueryListValue)visitor.visit(ctx.right);

        qc.popContextElement();
        return r;
    }

    public XQueryListValue evalFilter(@NotNull RpFilterContext ctx) {
        XQueryListValue res = new XQueryListValue();

        // Evaluate rp to get x
        XQueryListValue xs = (XQueryListValue)visitor.visit(ctx.rp());

        for(IXMLElement x : xs) {
            // Evaluate f from this context
            qc.pushContextElement(x);

            XQueryFilterValue y = (XQueryFilterValue)visitor.visit(ctx.f());
            qc.popContextElement();


            if(y == XQueryFilterValue.trueValue())
                res.add(x);
        }
        return res;
    }

    public XQueryListValue evalConcat(@NotNull RpConcatContext ctx) {
        XQueryListValue l = (XQueryListValue)visitor.visit(ctx.left);
        XQueryListValue r = (XQueryListValue)visitor.visit(ctx.right);

        l.addAll(r);
        return l;
    }
}
