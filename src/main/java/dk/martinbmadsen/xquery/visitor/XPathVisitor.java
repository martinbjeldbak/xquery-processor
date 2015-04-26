package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLDocument;
import dk.martinbmadsen.xquery.parser.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class XPathVisitor extends XPathBaseVisitor<List<IXMLElement>> {
    List<IXMLElement> elems = new ArrayList<>();

    @Override public List<IXMLElement> visitAp(@NotNull XPathParser.ApContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        XMLDocument document = new XMLDocument(ctx.fileName.getText());
        elems.add(document.root());

        switch(ctx.slash.getText()) {
            case "/":
                return visit(ctx.rp());
            case "//":
                break;
            default:
                Debugger.error("Oops, shouldn't be here");
                break;
        }
        return results;
    }


    @Override
    public List<IXMLElement> visitRpDot(@NotNull XPathParser.RpDotContext ctx) {
        return elems;
    }

    @Override
    public List<IXMLElement> visitRpDotDot(@NotNull XPathParser.RpDotDotContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : elems)
            results.add(x.parent());
        return unique(results);
    }

    @Override
    public List<IXMLElement> visitRpText(@NotNull XPathParser.RpTextContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        //TODO: Implement Text
        System.out.println("was");
        return elems;
    }

    @Override
    public List<IXMLElement> visitRpSlash(@NotNull XPathParser.RpSlashContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        switch (ctx.slash.getText()){
            case "/":
                elems = visit(ctx.left);
                results = visit(ctx.right);
                break;
            case "//":
                elems = visit(ctx.left);
                String tag = ctx.right.getText();

                List<IXMLElement> elems2 = new ArrayList<>(); // Intermediate array to hold elements
                List<IXMLElement> elems3 = new ArrayList<>(); // Array for all children
                elems3.addAll(elems);

                while (!elems.isEmpty()){
                    for (IXMLElement x : elems)
                        elems2.addAll(x.children()); // All all children at current level
                    elems3.addAll(elems2);
                    elems2.removeAll(elems); //Remove previous level of children
                    elems.clear();
                    elems.addAll(elems2); // Search next level of children

                }

                // Find all children with tag equal to RP2
                for (IXMLElement x : elems3){
                    if (x.tag().equals(tag))
                        results.add(x);
                }
        }

        return unique(results);
    }

    @Override
    public List<IXMLElement> visitRpConcat(@NotNull XPathParser.RpConcatContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        List<IXMLElement> elems2 = new ArrayList<>();
        elems2.addAll(elems); // Save old state
        results.addAll(visit(ctx.left));
        elems = elems2; // Set current state to new state
        results.addAll(visit(ctx.right));
        return results;
    }

    @Override
    public List<IXMLElement> visitRpTagName(@NotNull XPathParser.RpTagNameContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        String tag = ctx.getText();
        for (IXMLElement x : elems)
            results.addAll(x.children().stream().filter(c -> c.tag().equals(tag)).collect(Collectors.toList()));
        return results;
    }

    @Override
    public List<IXMLElement> visitRpFilter(@NotNull XPathParser.RpFilterContext ctx) {
        elems = visit(ctx.rp());

        return visit(ctx.f());
    }

    @Override
    public List<IXMLElement> visitRpWildcard(@NotNull XPathParser.RpWildcardContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : elems)
            results.addAll(x.children());
        return results;
    }

    @Override
    public List<IXMLElement> visitRpParenExpr(@NotNull XPathParser.RpParenExprContext ctx) {
        return visit(ctx.rp());
    }

    @Override
    public List<IXMLElement> visitRpAttr(@NotNull XPathParser.RpAttrContext ctx) {
        // TODO: Implement attr
        List<IXMLElement> results = new ArrayList<>();
        return results;
    }

    @Override
    public List<IXMLElement> visitFIdEqual(@NotNull XPathParser.FIdEqualContext ctx) {
        List<IXMLElement> elems2 = new ArrayList<>();
        elems2.addAll(elems); // Save old state
        List<IXMLElement> rp1 = new ArrayList<>();
        List<IXMLElement> rp2 = new ArrayList<>();
        rp1.addAll(visit(ctx.left));
        elems.clear();
        elems.addAll(elems2); // Set current state to new state
        rp2.addAll(visit(ctx.right));
        if (rp1.size() == rp2.size())
            for (int i = 0; i < rp1.size(); i++)
                if (rp1.get(i).getValue().equals(rp2.get(i).getValue()))
                    return elems2; //TODO: Fix this
        return elems2;
    }

    @Override
    public List<IXMLElement> visitFAnd(@NotNull XPathParser.FAndContext ctx) {
        return super.visitFAnd(ctx);
    }

    @Override
    public List<IXMLElement> visitFNot(@NotNull XPathParser.FNotContext ctx) {
        return super.visitFNot(ctx);
    }

    @Override
    public List<IXMLElement> visitFRp(@NotNull XPathParser.FRpContext ctx) {
        // /FM[P = "whatever" => Returner alle de FM elementer hvor P = "Whatever"

        return super.visitFRp(ctx);
    }

    @Override
    public List<IXMLElement> visitFParen(@NotNull XPathParser.FParenContext ctx) {
        return visit(ctx.fp());
    }

    @Override
    public List<IXMLElement> visitFOr(@NotNull XPathParser.FOrContext ctx) {
        return super.visitFOr(ctx);
    }

    @Override
    public List<IXMLElement> visitFValEqual(@NotNull XPathParser.FValEqualContext ctx) {
        List<IXMLElement> elems2 = new ArrayList<>();
        elems2.addAll(elems); // Save old state
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : elems2) {
            elems.clear();
            elems.add(x);
            List<IXMLElement> rp1 = visit(ctx.left);
            elems.clear();
            elems.addAll(elems2);
            List<IXMLElement> rp2 = visit(ctx.right); 
            for (IXMLElement e1 : rp1)
                for (IXMLElement e2 : rp2)
                    if (e1.getValue().equals(e2.getValue()))
                        results.add(x);

        }

        return results;
    }

    private List<IXMLElement> unique(List<IXMLElement> list){
        if (list == null)
            return list;
        List<IXMLElement> res2 = new ArrayList<>();
        List<String> addedRes = new ArrayList<>();
        res2.addAll(list);
        list.clear();

        for (int i = 0; i < res2.size(); i++) {
            String value = res2.get(i).getValue();
            if (addedRes.contains(value))
                continue;
            else {
                addedRes.add(value);
                list.add(res2.get(i));
            }
        }
        return list;
    }

}
