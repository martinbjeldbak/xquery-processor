package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.xquery.XMLTree.XMLDocument;
import dk.martinbmadsen.xquery.XMLTree.XMLElement;
import dk.martinbmadsen.xquery.parser.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.jdom2.Element;

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
                results.addAll(visit(ctx.rp()));
                break;
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
    public List<IXMLElement> visitRpText(@NotNull XPathParser.RpTextContext ctx) {
        return super.visitRpText(ctx);
    }

    @Override
    public List<IXMLElement> visitRpDotDot(@NotNull XPathParser.RpDotDotContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : elems)
            results.add(x.parent());
        return unique(results);
    }

    @Override
    public List<IXMLElement> visitRpSlash(@NotNull XPathParser.RpSlashContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        switch (ctx.slash.getText()){
            case "/":
                elems = visit(ctx.left);
                System.out.println(elems.get(0).tag() + " : " + ctx.right.getText());
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

                //Remove duplicates (by reference, not value)
                results = unique(results);

                break;
        }

        return results;
    }

    @Override
    public List<IXMLElement> visitRpConcat(@NotNull XPathParser.RpConcatContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        results.addAll(visit(ctx.left));
        results.addAll(visit(ctx.right));
        return results;
    }

    @Override
    public List<IXMLElement> visitF(@NotNull XPathParser.FContext ctx) {
        return super.visitF(ctx);
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
        return super.visitRpFilter(ctx);
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


/*

    @Override public List<IXMLElement> visitRp(@NotNull XPathParser.RpContext ctx) {
        System.out.println("Current token: " + parser.getCurrentToken().getText());
        TokenStream tokens = parser.getInputStream();
        Token token = tokens.getTokenSource().nextToken();
        System.out.println("ATN: " + ctx.invokingState);
        System.out.println("Current token2: "  + parser.getCurrentToken().getText());
        System.out.println("Next token: "  + token.getText());
        System.out.println("Identifier: "  + XPathParser.Identifier);
        List<IXMLElement> results = new ArrayList<>();
        switch (ctx.start.getType()) {
            case XPathParser.Identifier:
                String tag = ctx.getStart().getText();
                results = document.root().children().stream().filter(c -> c.tag().equals(tag)).collect(Collectors.toList());
                break;
            default:
                System.out.println(ctx.getText());
                System.out.println(ctx.getStart().getType());
                System.out.println(ctx.getStart().getTokenSource().nextToken().getType());
                System.out.println(ctx.getStop().getType() + "\n\n");


                for(int i = 0; i < 25; i++)
                    System.out.println(ctx.getTokens(i));
                System.out.println("lol fail");
                results.add(new XMLElement(new Element(ctx.getText())));
                break;
        }

        return results;
    }

    @Override public List<IXMLElement> visitF(@NotNull XPathParser.FContext ctx) {
        return visitChildren(ctx);
    }
*/
    private List<IXMLElement> unique(List<IXMLElement> list){
        List<IXMLElement> res2 = new ArrayList<>();
        res2.addAll(list);
        list.clear();
        for (IXMLElement x : res2)
            if(!list.contains(x))
                list.add(x);

        return list;
    }

}
