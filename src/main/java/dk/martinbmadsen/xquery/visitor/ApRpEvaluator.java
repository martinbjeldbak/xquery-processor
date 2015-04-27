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

import java.util.List;
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
                results.append((XQueryListValue)visitor.visit(ctx.rp()));
                break;
            case XQueryParser.SSLASH:
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
        return new XQueryListValue(qc.peekContextElement().children());
    }

    public XQueryListValue evalDot() {
        return new XQueryListValue(qc.peekContextElement());
    }

    public XQueryListValue evalDotDot() {
        IXMLElement parent = evalDot().get(0).parent();
        qc.popContextElement();
        return new XQueryListValue(parent);
    }

    public XQueryListValue evalText() {
        return new XQueryListValue(qc.peekContextElement().txt());
    }

    public XQueryListValue evalAttr(RpAttrContext ctx) {
        // TODO: Don't return a list value here, return an attribute value... or something!
        IXMLElement attrib = qc.peekContextElement().attrib(ctx.Identifier().getSymbol().getText());

        if(attrib == null)
            return new XQueryListValue(0);
        return new XQueryListValue(attrib);
    }

    public XQueryListValue evalParen(@NotNull RpParenExprContext ctx) {
        return new XQueryListValue((XQueryListValue)visitor.visit(ctx.rp()));
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
        XQueryListValue xs = (XQueryListValue)visitor.visit(ctx.left);

        for(IXMLElement x : xs) {
            qc.pushContextElement(x);
            XQueryListValue context = (XQueryListValue)visitor.visit(ctx.right);

            y.addAll(context);
        }
        return unique(y);
    }

    public XQueryListValue evalRpSlashSlash(@NotNull RpSlashContext ctx) {
        IXMLElement context = qc.peekContextElement();

        // Eval left hand side of // semantics (rp1 / rp2)
        XQueryListValue l = evalRpSlash(ctx);

        qc.pushContextElement(context);

        // Eval right hand side of // semantics (rp1 / * // rp2). This requires
        // building a new parse tree
        /**
         * Now, to compute the parse tree for right expr: rp1 / * // rp2
         *             rp
         *          /  |  \
         *        rp  // rp2
         *      / | \
         *    rp1 / rp
         *          |
         *          *
         */


        // Build above tree bottom up
        RpContext wcRoot = new RpContext();
        wcRoot.addChild(new RpWildcardContext(wcRoot));

        RpContext slashRoot = new RpContext();
        RpSlashContext slashCtx = new RpSlashContext(slashRoot);
        slashRoot.addChild(slashCtx);
        slashCtx.addChild(ctx.left); // not sure if you need to addChild
        slashCtx.addChild(ctx.right);
        slashCtx.left = ctx.left;
        slashCtx.right = wcRoot;

        List<IXMLElement> intermediatRes1 = evalRpSlash(slashCtx);

        RpContext ssRoot = new RpContext();
        RpSlashContext ssCtx = new RpSlashContext(ssRoot);
        ssCtx.addChild(slashCtx);

        ssCtx.addChild(ctx.right);
        ssCtx.left = slashCtx;
        ssCtx.right = ctx.right;
        ssCtx.slash = ctx.slash;

        System.out.println(intermediatRes1.size());
        /*
        RpContext root = new RpContext();
        RpSlashContext ssCtx = new RpSlashContext(root);
        ssCtx.addChild(slashCtx); // not sure if you need to addChild
        ssCtx.addChild(node.right);
        ssCtx.left = slashCtx;
        ssCtx.right = node.right;

        //List<IXMLElement> r = evalRpSlashSlash(wcRoot);

        //l.addAll(r);
         */

        return unique(l);
    }

    public XQueryListValue evalFilter(@NotNull RpFilterContext ctx) {
        XQueryListValue res = new XQueryListValue();

        // Evaluate rp to get x
        XQueryListValue xs = (XQueryListValue)visitor.visit(ctx.rp());

        for(IXMLElement x : xs) {
            qc.pushContextElement(x); // Get context node

            // Evaluate f from this context
            XQueryFilterValue y = (XQueryFilterValue)visitor.visit(ctx.f());

            if(y == XQueryFilterValue.trueValue())
                res.add(x);
        }
        return res;
    }

    public XQueryListValue evalConcat(@NotNull RpConcatContext ctx) {
        // Save context XML element n
        IXMLElement n = qc.peekContextElement();

        XQueryListValue l = (XQueryListValue)visitor.visit(ctx.left);

        // Push element n back onto our context stack (since r also has to be evalauted from n)
        qc.pushContextElement(n);

        XQueryListValue r = (XQueryListValue)visitor.visit(ctx.right);

        l.addAll(r);
        return l;
    }

    /**
     * TODO: Implement this method
     * Removes duplicate elements
     * @param elems element list to be checked
     * @return a sanitized list with duplicate elements removed
     */
    private XQueryListValue unique(XQueryListValue elems) {

        /*
        List<IXMLElement> result = buildResult(elems.size());

        for(IXMLElement i : elems) {
            for(IXMLElement j : elems) {
                if(i.equals(j))
                    break;
                result.add(i);
            }
        }
        */
        return elems;
    }
}
