package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.xquery.XMLTree.XMLDocument;
import dk.martinbmadsen.xquery.debug.Debug;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class XQueryVisitor extends XQueryBaseVisitor<List<IXMLElement>> {
    private XMLDocument document;
    private Stack<IXMLElement> ctxElems = new Stack<>(); // context elements

    @Override
    public List<IXMLElement> visitAp(@NotNull XQueryParser.ApContext ctx) {
        this.document = new XMLDocument(ctx.fileName.getText());
        this.ctxElems.push(this.document.root());
        List<IXMLElement> results = new ArrayList<>();

        switch(ctx.slash.getType()) {
            case XQueryLexer.SLASH:
                results.addAll(visit(ctx.rp()));
                break;
            case XQueryLexer.SSLASH:
                break;
            default:
                Debug.debug("Oops, shouldn't be here");
                break;
        }
        return results;
    }

    @Override
    public List<IXMLElement> visitRp(@NotNull XQueryParser.RpContext ctx) {
        List<IXMLElement> result = new ArrayList<>();
        IXMLElement ctxEl = this.ctxElems.pop();

        if(ctx.tagName != null) {
            String tagName = ctx.tagName.getText();

            result.addAll(ctxEl.children().stream().filter(
                    c -> c.tag().equals(tagName)
            ).collect(Collectors.toList()));
        }
        else if (ctx.mult != null) {
            result.addAll(ctxEl.children());
        }
        else if (ctx.dot != null) {
            result.add(ctxEl);
        }
        else if (ctx.dotdot != null) {
            result.add(ctxEl.parent());
        }
        else if (ctx.text != null) {
            // TODO: Need to add this to the result somehow... (maybe cast to XMLElement?)
            Debug.result("text(): " + ctxEl.txt());
        }
        else {
            Debug.debug("Error: Could not match syntax: " + ctx.getText());
        }

        return result;
    }
}
