package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.xquery.XMLTree.XMLDocument;
import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.misc.NotNull;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Evaluator {
    private QueryContext qc;
    private XQueryBaseVisitor<List<IXMLElement>> visitor;

    public Evaluator(QueryContext qc, XQueryBaseVisitor<List<IXMLElement>> visitor) {
        this.qc = qc;
        this.visitor = visitor;
    }

    public List<IXMLElement> evalAp(@NotNull RuleContext ctx) {
        if(!(ctx instanceof ApContext))
            Debugger.error("Context given not of type ApContext");
        ApContext node = (ApContext) ctx;

        XMLDocument document = new XMLDocument(node.fileName.getText());
        qc.pushContextElement(document.root());
        List<IXMLElement> results = buildResult();

        switch(node.slash.getType()) {
            case XQueryParser.SLASH:
                results.addAll(visitor.visit(node.rp()));
                break;
            case XQueryParser.SSLASH:
                break;
            default:
                Debugger.error("Oops, shouldn't be here");
                break;
        }
        return results;
    }

    public List<IXMLElement> evalTagName(@NotNull RuleContext ctx) {
        if(!(ctx instanceof RpTagNameContext))
            Debugger.error("Context given not of type RpTagNameContext");

        String tagName = ctx.getText();

        return evalWildCard().stream().filter(
                c -> c.tag().equals(tagName)
        ).collect(Collectors.toList());
    }

    public List<IXMLElement> evalWildCard() {
        return qc.peekContextElement().children();
    }

    public List<IXMLElement> evalDot() {
        return buildResult(qc.peekContextElement());
    }

    public List<IXMLElement> evalDotDot() {
        IXMLElement parent = evalDot().get(0).parent();
        qc.popContextElement();
        return buildResult(parent);
    }

    public List<IXMLElement> evalRpParen(@NotNull RuleContext ctx) {
        if(!(ctx instanceof RpParenExprContext))
            Debugger.error("Context given not of type RpTagNameContext");
        RpParenExprContext node = (RpParenExprContext) ctx;
        return visitor.visit(node.rp());
    }


    public List<IXMLElement> evalRpSlash(@NotNull RuleContext ctx) {
        if(!(ctx instanceof RpSlashContext))
            Debugger.error("Context given not of type RpSlashContext");
        RpSlashContext node = (RpSlashContext) ctx;

        List<IXMLElement> y = buildResult();

        List<IXMLElement> x = visitor.visit(node.left);

        for(IXMLElement res : x) {
            qc.pushContextElement(res);
            List<IXMLElement> context = visitor.visit(node.right);

            y.addAll(context);
        }
        return y;
    }

    public List<IXMLElement> evalRpSlashSlash(@NotNull RuleContext ctx) {
        if(!(ctx instanceof RpSlashContext))
            Debugger.error("Context given not of type RpSlashContext");


        IXMLElement context = qc.peekContextElement();


        RpSlashContext node = (RpSlashContext)ctx;

        // Eval left hand side of // semantics (rp1 / rp2)
        List<IXMLElement> l = evalRpSlash(ctx);

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

        return l;
    }

    private List<IXMLElement> buildResult() {
        return new ArrayList<>();
    }

    /**
     * Returns a new instance of whatever list I'm in the mood for, currently {@link ArrayList} for
     * use in building results.
     * @return a newly instanciated {@link ArrayList}
     */
    private List<IXMLElement> buildResult(int size) {
        return new ArrayList<>(size);
    }

    private List<IXMLElement> buildResult(IXMLElement elem) {
        List<IXMLElement> res = buildResult(1);
        res.add(elem);
        return res;
    }

    /**
     * Removes duplicate elements
     * @param elems element list to be checked
     * @return a santized list with duplicate elements removed
     */
    private List<IXMLElement> unique(List<IXMLElement> elems) {
        // Because I couldn't figure out how to copy Lists...
        List<IXMLElement> result = buildResult(elems.size());
        result.addAll(elems);

        for(IXMLElement i : elems) {
            for(IXMLElement j : elems) {
                if(i.equals(j))
                    continue;
                result.add(j);
            }
        }
        return result;
    }
}
