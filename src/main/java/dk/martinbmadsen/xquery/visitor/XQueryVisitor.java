package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.xquery.XMLTree.XMLDocument;
import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryLexer;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class XQueryVisitor extends XQueryBaseVisitor<List<IXMLElement>> {
    private QueryContext context = new QueryContext();

    @Override
    public List<IXMLElement> visitAp(@NotNull XQueryParser.ApContext ctx) {
        XMLDocument document = new XMLDocument(ctx.fileName.getText());
        context.addContextElement(document.root());
        List<IXMLElement> results = new ArrayList<>();

        switch(ctx.slash.getType()) {
            case XQueryLexer.SLASH:
                results.addAll(visit(ctx.rp()));
                break;
            case XQueryLexer.SSLASH:
                break;
            default:
                Debugger.error("Oops, shouldn't be here");
                break;
        }
        return results;
    }

    @Override
    public List<IXMLElement> visitRpTagName(@NotNull XQueryParser.RpTagNameContext ctx) {
        IXMLElement ctxEl = context.getContextElement();
        String tagName = ctx.getText();

        return buildResult(ctxEl.children().stream().filter(
                c -> c.tag().equals(tagName)
        ).collect(Collectors.toList()));
    }

    @Override
    public List<IXMLElement> visitRpWildcard(@NotNull XQueryParser.RpWildcardContext ctx) {
        return buildResult(context.getContextElement().children());
    }

    @Override
    public List<IXMLElement> visitRpDot(@NotNull XQueryParser.RpDotContext ctx) {
        return buildResult(context.getContextElement());
    }

    @Override
    public List<IXMLElement> visitRpDotDot(@NotNull XQueryParser.RpDotDotContext ctx) {
        return buildResult(context.getContextElement().parent());
    }

    @Override
    public List<IXMLElement> visitRpText(@NotNull XQueryParser.RpTextContext ctx) {
        return buildResult(context.getContextElement().txt());
    }

    @Override
    public List<IXMLElement> visitRpParenExpr(@NotNull XQueryParser.RpParenExprContext ctx) {
        return visit(ctx.rp());
    }

    @Override
    public List<IXMLElement> visitRpSlash(@NotNull XQueryParser.RpSlashContext ctx) {
        List<IXMLElement> y = buildResult();
        List<IXMLElement> x = visit(ctx.left);

        for(IXMLElement res : x) {
            context.addContextElement(res);
            y.addAll(visit(ctx.right));
        }

        return unique(y);
    }

    @Override
    public List<IXMLElement> visitRpSlashSlash(@NotNull XQueryParser.RpSlashSlashContext ctx) {
        return super.visitRpSlashSlash(ctx);
    }

    @Override
    public List<IXMLElement> visitF(@NotNull XQueryParser.FContext ctx) {
        return super.visitF(ctx);
    }

    @Override
    public List<IXMLElement> visitRpConcat(@NotNull XQueryParser.RpConcatContext ctx) {
        return super.visitRpConcat(ctx);
    }

    @Override
    public List<IXMLElement> visitRpFilter(@NotNull XQueryParser.RpFilterContext ctx) {
        return super.visitRpFilter(ctx);
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

    private List<IXMLElement> buildResult(List<IXMLElement> elems) {
        List<IXMLElement> res = buildResult(elems.size());
        res.addAll(elems);
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
