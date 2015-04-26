package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLDocument;
import dk.martinbmadsen.xquery.parser.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class XPathVisitor extends XPathBaseVisitor<List<IXMLElement>> {
    List<IXMLElement> state = new ArrayList<>();

    @Override public List<IXMLElement> visitAp(@NotNull XPathParser.ApContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        XMLDocument document = new XMLDocument(ctx.fileName.getText());
        state.add(document.root());

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
        return state;
    }

    @Override
    public List<IXMLElement> visitRpDotDot(@NotNull XPathParser.RpDotDotContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : state)
            results.add(x.parent());
        return unique(results);
    }

    @Override
    public List<IXMLElement> visitRpText(@NotNull XPathParser.RpTextContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        //TODO: Implement Text
        System.out.println("was");
        return state;
    }

    @Override
    public List<IXMLElement> visitRpSlash(@NotNull XPathParser.RpSlashContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        switch (ctx.slash.getText()){
            case "/":
                state = visit(ctx.left);
                results = visit(ctx.right);
                break;
            case "//":
                state = visit(ctx.left);
                String tag = ctx.right.getText();

                List<IXMLElement> temp = new ArrayList<>(); // Intermediate array to hold elements
                List<IXMLElement> decendants = new ArrayList<>(); // Array for all children
                decendants.addAll(state);

                while (!state.isEmpty()){
                    for (IXMLElement x : state)
                        temp.addAll(x.children()); // All all children at current level
                    decendants.addAll(temp);
                    temp.removeAll(state); //Remove previous level of children
                    state.clear();
                    state.addAll(temp); // Search next level of children

                }

                // Find all children with tag equal to RP2
                for (IXMLElement x : decendants){
                    if (x.tag().equals(tag))
                        results.add(x);
                }
        }

        return unique(results);
    }

    @Override
    public List<IXMLElement> visitRpConcat(@NotNull XPathParser.RpConcatContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        List<IXMLElement> oldState = new ArrayList<>();
        oldState.addAll(state); // Save old state
        results.addAll(visit(ctx.left));
        state = oldState; // Set current state to new state
        results.addAll(visit(ctx.right));
        return results;
    }

    @Override
    public List<IXMLElement> visitRpTagName(@NotNull XPathParser.RpTagNameContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        String tag = ctx.getText();
        for (IXMLElement x : state)
            results.addAll(x.children().stream().filter(c -> c.tag().equals(tag)).collect(Collectors.toList()));
        return results;
    }

    @Override
    public List<IXMLElement> visitRpFilter(@NotNull XPathParser.RpFilterContext ctx) {
        state = visit(ctx.rp());

        return visit(ctx.f());
    }

    @Override
    public List<IXMLElement> visitRpWildcard(@NotNull XPathParser.RpWildcardContext ctx) {
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : state)
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
        List<IXMLElement> oldState = new ArrayList<>();
        oldState.addAll(state); // Save old state
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : oldState) {
            state.clear();
            state.add(x);
            List<IXMLElement> rp1 = visit(ctx.left);
            state.clear();
            state.addAll(oldState);
            List<IXMLElement> rp2 = visit(ctx.right); 
            for (IXMLElement e1 : rp1)
                for (IXMLElement e2 : rp2)
                    if (e1.equals(e2))
                        results.add(x);

        }

        return results;
    }

    @Override
    public List<IXMLElement> visitFAnd(@NotNull XPathParser.FAndContext ctx) {
        List<IXMLElement> oldState = new ArrayList<>();
        oldState.addAll(state); // Save old state
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : oldState) {
            state.clear();
            state.add(x);
            List<IXMLElement> rp1 = visit(ctx.left);
            state.clear();
            state.add(x);
            List<IXMLElement> rp2 = visit(ctx.right); 
            if(rp1.size() > 0 && rp2.size() > 0)
                results.add(x);
        }

        return results;
    }

    @Override
    public List<IXMLElement> visitFNot(@NotNull XPathParser.FNotContext ctx) {
        List<IXMLElement> oldState = new ArrayList<>();
        oldState.addAll(state); // Save old state
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : oldState) {
            state.clear();
            state.add(x);
            List<IXMLElement> f = visit(ctx.f());
            if(f.size() == 0)
                results.add(x);
        }

        return results;
    }

    @Override
    public List<IXMLElement> visitFRp(@NotNull XPathParser.FRpContext ctx) {
        List<IXMLElement> oldState = new ArrayList<>();
        oldState.addAll(state); // Save old state
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : oldState) {
            state.clear();
            state.add(x);
            List<IXMLElement> rp = visit(ctx.rp());
            if(rp.size() > 0)
                results.add(x);
        }

        return results;
    }

    @Override
    public List<IXMLElement> visitFParen(@NotNull XPathParser.FParenContext ctx) {
        return visit(ctx.f());
    }

    @Override
    public List<IXMLElement> visitFOr(@NotNull XPathParser.FOrContext ctx) {
        List<IXMLElement> oldState = new ArrayList<>();
        oldState.addAll(state); // Save old state
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : oldState) {
            state.clear();
            state.add(x);
            List<IXMLElement> rp1 = visit(ctx.left);
            state.clear();
            state.add(x);
            List<IXMLElement> rp2 = visit(ctx.right); 
            if(rp1.size() > 0 || rp2.size() > 0)
                results.add(x);
        }

        return results;
    }

    @Override
    public List<IXMLElement> visitFValEqual(@NotNull XPathParser.FValEqualContext ctx) {
        List<IXMLElement> oldState = new ArrayList<>();
        oldState.addAll(state); // Save old state
        List<IXMLElement> results = new ArrayList<>();
        for (IXMLElement x : oldState) {
            state.clear();
            state.add(x);
            List<IXMLElement> rp1 = visit(ctx.left);
            state.clear();
            state.addAll(oldState);
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
