package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.VarEnvironment;
import dk.martinbmadsen.xquery.value.XQueryList;
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

    public XQueryList evalParen(@NotNull XqParenExprContext ctx){
        return (XQueryList) visitor.visit(ctx.xq());
    }

    public XQueryList evalVar(@NotNull XqVarContext ctx){
        return qc.getVar(ctx.getText());
    }

    public XQueryList evalConcat(@NotNull XqConcatContext ctx){
        XQueryList l = (XQueryList)visitor.visit(ctx.left);
        XQueryList r = (XQueryList)visitor.visit(ctx.right);

        l.addAll(r);
        return l;
    }
    public XQueryList evalSlash(@NotNull XqSlashContext ctx){
        XQueryList xq = (XQueryList)visitor.visit(ctx.xq());
        qc.pushContextElement(xq);
        XQueryList rp = (XQueryList)visitor.visit(ctx.rp());
        qc.popContextElement();
        return rp.unique();
    }
    public XQueryList evalTagname(@NotNull XqTagNameContext ctx) {
        XQueryList xq = (XQueryList)visitor.visit(ctx.xq());
        return new XQueryList(new XMLElement(ctx.tagName.getText(), xq));
    }

    public XQueryList evalFLWR(@NotNull XqFLWRContext ctx) {
        return null;
    }

    public XQueryList evalLet(@NotNull XqLetContext ctx) {
        qc.pushVarEnv((VarEnvironment) visitor.visitLetClause(ctx.letClause()));
        XQueryList xq = (XQueryList)visitor.visit(ctx.xq());
        qc.popVarEnv();
        return xq;
    }
}
