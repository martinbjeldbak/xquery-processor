package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.utils.debug.Debugger;
import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.VarEnvironment;
import dk.martinbmadsen.xquery.value.XQueryList;
import dk.martinbmadsen.xquery.xmltree.IXMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLElement;
import dk.martinbmadsen.xquery.xmltree.XMLText;
import org.antlr.v4.runtime.misc.NotNull;

public class XqEvaluator extends XQueryEvaluator {
    public XqEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryList evalStringConstant(@NotNull XqStringConstantContext ctx){
        return new XQueryList(new XMLText(ctx.StringLiteral().getText()));
    }

    public XQueryList evalAp(@NotNull XqApContext ctx) {
        return (XQueryList)visitor.visit(ctx.ap());
    }

    public XQueryList evalParen(@NotNull XqParenExprContext ctx) {
        return (XQueryList)visitor.visit(ctx.xq());
    }

    public XQueryList evalVar(@NotNull XqVarContext ctx) {
        return qc.getVar(ctx.getText());
    }

    public XQueryList evalConcat(@NotNull XqConcatContext ctx){
        //qc.openScope();
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        //qc.closeScope();

        //qc.openScope();
        XQueryList r = (XQueryList)visitor.visit(ctx.right);
        //qc.closeScope();

        l.addAll(r);
        return l;
    }

    private XQueryList evalXqSlash(@NotNull XqSlashContext ctx) {
        XQueryList xq = (XQueryList)visitor.visit(ctx.xq());

        qc.pushContextElement(xq);
        XQueryList rp = (XQueryList)visitor.visit(ctx.rp());
        qc.popContextElement();

        return rp.unique();
    }

    private XQueryList evalXqSlashSlash(@NotNull XqSlashContext ctx) {
        // TODO: Implement
        Debugger.error("XQ // has not been implemented yet");
        return null;
    }

    public XQueryList evalSlashes(@NotNull XqSlashContext ctx) {
        XQueryList results = new XQueryList();
        switch(ctx.slash.getType()) {
            case XQueryParser.SLASH:
                results = evalXqSlash(ctx);
                break;
            case XQueryParser.SSLASH:
                results = evalXqSlashSlash(ctx);
                break;
            default:
                Debugger.error("Oops, shouldn't be here");
                break;
        }
        return results;
    }

    public XQueryList evalTagname(@NotNull XqTagNameContext ctx) {
        if(!ctx.open.getText().equals(ctx.close.getText()))
            Debugger.error(ctx.open.getText() + "is not closed properly. You closed it with " + ctx.close.getText());

        XQueryList res = new XQueryList();
        XQueryList xq = (XQueryList)visitor.visit(ctx.xq());

        // Figure out whether to add result as text or child element
        for(IXMLElement v : xq) {
            if(v instanceof XMLText)
                res.add(new XMLElement(ctx.open.getText(), (XMLText)v));
            else if(v instanceof XMLElement)
                res.add(new XMLElement(ctx.open.getText(), (XMLElement)v));
        }

        return res;
    }

    public XQueryList evalFLWR(@NotNull XqFLWRContext ctx) {
        VarEnvironment veFor = (VarEnvironment)visitor.visit(ctx.forClause());
        VarEnvironment veLet = (VarEnvironment)visitor.visit(ctx.letClause());


        visitor.visit(ctx.whereClause());
        visitor.visit(ctx.returnClause());

        return null;
    }

    public XQueryList evalLet(@NotNull XqLetContext ctx) {
        // Changes a bunch of stuff within the global scope
        VarEnvironment ve = (VarEnvironment)visitor.visit(ctx.letClause());

        qc.pushVarEnv(ve);

        XQueryList res = (XQueryList)visitor.visit(ctx.xq());

        qc.popVarEnv();

        return res;
    }
}
