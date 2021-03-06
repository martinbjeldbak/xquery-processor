package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryFilter;
import dk.martinbmadsen.xquery.value.XQueryList;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLDocument;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.stream.Collectors;

public class ApRpEvaluator extends XQueryEvaluator {

    public ApRpEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryList evalAp(@NotNull ApContext ctx) {
        XMLDocument document = new XMLDocument(ctx.fileName.getText());
        XQueryList results = new XQueryList();

        /* Replace the below code with this commented out code
         * in case documents roots have to excplictly be referenced
         * e.g. doc("j_caesar.xml")/PLAY/TITLE/text() instead of
         *      doc("j_caesar.xml")/TITLE/text() which is the current setting
        XMLElement root = new XMLElement("root");
        root.add(document.root());
        qc.pushContextElement(root);
        */

        qc.pushContextElement(document.root());

        switch(ctx.slash.getType()) {
            case XQueryParser.SLASH:
                results.addAll((XQueryList)visitor.visit(ctx.rp()));
                break;
            case XQueryParser.SSLASH:
                qc.pushContextElement(document.root().descendants());
                results.addAll( (XQueryList)visitor.visit(ctx.rp()) );
                break;
            default:
                Debugger.error("Oops, shouldn't be here");
                break;
        }
        return results.unique();
    }

    public XQueryList evalTagName(@NotNull RpTagNameContext ctx) {
        String tagName = ctx.getText();

        return evalWildCard().stream().filter(
                e -> e.tag().equals(tagName)
        ).collect(Collectors.toCollection(XQueryList::new));
    }

    public XQueryList evalWildCard() {
        XQueryList res = new XQueryList();

        for(IXMLElement context : qc.peekContextElement())
            res.addAll(context.children());
        return res;
    }

    public XQueryList evalDot() {
        return qc.peekContextElement();
    }

    public XQueryList evalDotDot() {
        return qc.peekContextElement().stream().map(
                e -> e.parent().get(0)
        ).collect(Collectors.toCollection(XQueryList::new)).unique();
        /* ^^^ is equal to vvv
        XQueryList res = new XQueryList();
        for(IXMLElement e : qc.peekContextElement()) {
            res.add(e.parent().get(0));
        }
        return res.unique();
        */
    }

    public XQueryList evalText() {
        return qc.peekContextElement().stream().map(
                IXMLElement::txt).collect(Collectors.toCollection(XQueryList::new));
        /* ^^^is equal to vvv
        XQueryList res = new XQueryList();
        for(IXMLElement x : qc.peekContextElement()) {
            res.add(x.txt());
        }
        return res;
         */
    }

    public XQueryList evalAttr(RpAttrContext ctx) {
        // TODO: Don't return a list value here, return an attribute value... or something!
        String attrib = ctx.Identifier().getSymbol().getText();
        XQueryList res = new XQueryList(qc.peekContextElement().size());

        // If @ followed by nothing
        if(attrib == null)
            return res;

        for(IXMLElement x : qc.peekContextElement()) {
            if(x.attrib(attrib) != null)
                res.add(x.attrib(attrib));
        }
        return res;
    }

    public XQueryList evalParen(@NotNull RpParenExprContext ctx) {
        return (XQueryList)visitor.visit(ctx.rp());
    }

    public XQueryList evalSlashes(@NotNull RpSlashContext ctx) {
        XQueryList results = new XQueryList();
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

    private XQueryList evalRpSlash(@NotNull RpSlashContext ctx) {
        XQueryList y = new XQueryList();
        XQueryList x = (XQueryList)visitor.visit(ctx.left);

        qc.pushContextElement(x);
        XQueryList context = (XQueryList)visitor.visit(ctx.right);
        y.addAll(context);

        qc.popContextElement();
        return y.unique();
    }

    private XQueryList evalRpSlashSlash(@NotNull RpSlashContext ctx) {
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        XQueryList descendants = new XQueryList();

        for(IXMLElement x : l) {
            descendants.addAll(x.descendants());
        }

        qc.pushContextElement(descendants);
        XQueryList r = (XQueryList)visitor.visit(ctx.right);
        qc.popContextElement();

        return r.unique();
    }

    public XQueryList evalFilter(@NotNull RpFilterContext ctx) {
        XQueryList res = new XQueryList();

        // Evaluate rp to get x
        XQueryList xs = (XQueryList)visitor.visit(ctx.rp());

        for(IXMLElement x : xs) {
            qc.pushContextElement(x);
            XQueryFilter y = (XQueryFilter)visitor.visit(ctx.f());
            qc.popContextElement();

            if(y == XQueryFilter.trueValue())
                res.add(x);
        }
        return res;
    }

    public XQueryList evalConcat(@NotNull RpConcatContext ctx) {
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        XQueryList r = (XQueryList)visitor.visit(ctx.right);

        l.addAll(r);
        return l;
    }
}
