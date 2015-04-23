package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.XMLTree.IXMLElement;
import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

public class XQueryVisitor extends XQueryBaseVisitor<List<IXMLElement>> {
    private QueryContext qc = new QueryContext();
    private Evaluator e = new Evaluator(qc, this);

    @Override
    public List<IXMLElement> visitAp(@NotNull XQueryParser.ApContext ctx) {
        return e.evalAp(ctx);
    }

    @Override
    public List<IXMLElement> visitRpTagName(@NotNull XQueryParser.RpTagNameContext ctx) {
        return e.evalTagName(ctx);
    }

    @Override
    public List<IXMLElement> visitRpWildcard(@NotNull XQueryParser.RpWildcardContext ctx) {
        return e.evalWildCard();
    }

    @Override
    public List<IXMLElement> visitRpDot(@NotNull XQueryParser.RpDotContext ctx) {
        return e.evalDot();
    }

    @Override
    public List<IXMLElement> visitRpDotDot(@NotNull XQueryParser.RpDotDotContext ctx) {
        return e.evalDotDot();
    }

    @Override
    public List<IXMLElement> visitRpText(@NotNull XQueryParser.RpTextContext ctx) {
        return buildResult(qc.peekContextElement().txt());
    }

    @Override
    public List<IXMLElement> visitRpParenExpr(@NotNull XQueryParser.RpParenExprContext ctx) {
        return e.evalRpParen(ctx);
    }

    @Override
    public List<IXMLElement> visitRpSlash(@NotNull XQueryParser.RpSlashContext ctx) {
        List<IXMLElement> results = buildResult();
        switch(ctx.slash.getType()) {
            case XQueryParser.SLASH:
                results = e.evalRpSlash(ctx);
                break;
            case XQueryParser.SSLASH:
                results = e.evalRpSlashSlash(ctx);
                break;
            default:
                Debugger.error("Oops, shouldn't be here");
                break;
        }
        return results;
    }


    @Override
    public List<IXMLElement> visitRpFilter(@NotNull XQueryParser.RpFilterContext ctx) {
        // Evaluate rp to get x
        List<IXMLElement> x = visit(ctx.rp());

        // Use this list of nodes as context when evaluating f
        qc.pushContextElements(x);

        List<IXMLElement> y = visit(ctx.f());

        /**
         * Concatenate the result of evaluating the relative path and the result
         * of evaluating the filter
         */
        x.addAll(y);

        return x;
    }

    @Override
    public List<IXMLElement> visitRpConcat(@NotNull XQueryParser.RpConcatContext ctx) {
        // Save context XML element n
        IXMLElement n = qc.peekContextElement();

        List<IXMLElement> l = visit(ctx.left);

        // Push element n back onto our context stack (since r also has to be evalauted from n)
        qc.pushContextElement(n);

        List<IXMLElement> r = visit(ctx.right);

        l.addAll(r);

        return l;
    }

    @Override
    public List<IXMLElement> visitF(@NotNull XQueryParser.FContext ctx) {
        return super.visitF(ctx);
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
}
