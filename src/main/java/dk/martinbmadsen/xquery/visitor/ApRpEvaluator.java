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
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ApRpEvaluator extends XQueryEvaluator {

    public ApRpEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryListValue evalAp(@NotNull RuleContext ctx) {
        if(!(ctx instanceof ApContext))
            Debugger.error("Context given not of type ApContext");
        ApContext node = (ApContext) ctx;

        XMLDocument document = new XMLDocument(node.fileName.getText());
        qc.pushContextElement(document.root());
        XQueryListValue results = new XQueryListValue();

        switch(node.slash.getType()) {
            case XQueryParser.SLASH:
                results.append((XQueryListValue)visitor.visit(node.rp()));
                break;
            case XQueryParser.SSLASH:
                break;
            default:
                Debugger.error("Oops, shouldn't be here");
                break;
        }
        return results;
    }

    public XQueryListValue evalTagName(@NotNull RuleContext ctx) {
        if(!(ctx instanceof RpTagNameContext))
            Debugger.error("Context given not of type RpTagNameContext");

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

    public XQueryListValue evalAttr(RpContext ctx) {
        if(!(ctx instanceof RpAttrContext))
            Debugger.error("ctx not an instance of RpAttrContext");
        RpAttrContext node = (RpAttrContext) ctx;

        return new XQueryListValue(qc.peekContextElement().attrib(node.Identifier().getSymbol().getText()));
    }

    public XQueryListValue evalRpParen(@NotNull RuleContext ctx) {
        if(!(ctx instanceof RpParenExprContext))
            Debugger.error("Context given not of type RpTagNameContext");
        RpParenExprContext node = (RpParenExprContext) ctx;
        return new XQueryListValue((XQueryListValue)visitor.visit(node.rp()));
    }

    public XQueryListValue evalRpSlashes(@NotNull RuleContext ctx) {
        if(!(ctx instanceof RpSlashContext))
            Debugger.error("Context given not of type RpSlashContext");
        RpSlashContext node = (RpSlashContext) ctx;

        XQueryListValue results = new XQueryListValue();
        switch(node.slash.getType()) {
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

    public XQueryListValue evalRpSlash(@NotNull RuleContext ctx) {
        if(!(ctx instanceof RpSlashContext))
            Debugger.error("Context given not of type RpSlashContext");
        RpSlashContext node = (RpSlashContext) ctx;

        XQueryListValue y = new XQueryListValue();
        XQueryListValue xs = (XQueryListValue)visitor.visit(node.left);

        for(IXMLElement x : xs) {
            qc.pushContextElement(x);
            XQueryListValue context = (XQueryListValue)visitor.visit(node.right);

            y.addAll(context);
        }
        return unique(y);
    }

    public XQueryListValue evalRpSlashSlash(@NotNull RuleContext ctx) {
        if(!(ctx instanceof RpSlashContext))
            Debugger.error("Context given not of type RpSlashContext");

        IXMLElement context = qc.peekContextElement();


        RpSlashContext node = (RpSlashContext)ctx;

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
        slashCtx.addChild(node.left); // not sure if you need to addChild
        slashCtx.addChild(node.right);
        slashCtx.left = node.left;
        slashCtx.right = wcRoot;

        List<IXMLElement> intermediatRes1 = evalRpSlash(slashCtx);

        RpContext ssRoot = new RpContext();
        RpSlashContext ssCtx = new RpSlashContext(ssRoot);
        ssCtx.addChild(slashCtx);

        ssCtx.addChild(node.right);
        ssCtx.left = slashCtx;
        ssCtx.right = node.right;
        ssCtx.slash = node.slash;

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

    public XQueryListValue evalRpFilter(@NotNull RpContext ctx) {
        if(!(ctx instanceof RpFilterContext))
            Debugger.error("Context node needs to be an instance of RpFilterContext");
        RpFilterContext node = ((RpFilterContext) ctx);

        XQueryListValue res = new XQueryListValue();

        // Evaluate rp to get x
        XQueryListValue xs = (XQueryListValue)visitor.visit(node.rp());

        for(IXMLElement x : xs) {
            qc.pushContextElement(x); // Get context node

            // Evaluate f from this context
            XQueryFilterValue y = (XQueryFilterValue)visitor.visit(node.f());

            if(y == XQueryFilterValue.trueValue())
                res.add(x);
        }
        return res;
    }

    public XQueryListValue evalConcat(@NotNull RpContext ctx) {
        if(!(ctx instanceof  RpConcatContext))
            Debugger.error("Context node needs to be an instance of RpConcatContext");
        RpConcatContext node = ((RpConcatContext) ctx);


        // Save context XML element n
        IXMLElement n = qc.peekContextElement();

        XQueryListValue l = (XQueryListValue)visitor.visit(node.left);

        // Push element n back onto our context stack (since r also has to be evalauted from n)
        qc.pushContextElement(n);

        XQueryListValue r = (XQueryListValue)visitor.visit(node.right);

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
