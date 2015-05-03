package dk.martinbmadsen.xquery.visitor;

import dk.martinbmadsen.xquery.context.QueryContext;
import dk.martinbmadsen.xquery.context.VarEnvironment;
import dk.martinbmadsen.xquery.parser.XQueryBaseVisitor;
import dk.martinbmadsen.xquery.parser.XQueryParser.*;
import dk.martinbmadsen.xquery.value.IXQueryValue;
import dk.martinbmadsen.xquery.value.XQueryListValue;
import dk.martinbmadsen.xquery.value.XQueryTextValue;
import dk.martinbmadsen.xquery.xmltree.XMLElement;
import org.antlr.v4.runtime.misc.NotNull;

public class XqEvaluator extends XQueryEvaluator {
    public XqEvaluator(XQueryBaseVisitor<IXQueryValue> visitor, QueryContext qc) {
        super(visitor, qc);
    }

    public XQueryTextValue evalStringConstant (@NotNull XqStringConstantContext ctx){
        return new XQueryTextValue(ctx.getText());
    }

    public XQueryListValue evalAp (@NotNull XqApContext ctx) {
        return (XQueryListValue)visitor.visitAp(ctx.ap());
    }

    public XQueryListValue evalParen(@NotNull XqParenExprContext ctx){
        return (XQueryListValue) visitor.visit(ctx.xq());
    }

    public XQueryListValue evalVar(@NotNull XqVarContext ctx){
        return qc.getVar(ctx.getText());
    }

    public XQueryListValue evalConcat(@NotNull XqConcatContext ctx){
        XQueryListValue l = (XQueryListValue)visitor.visit(ctx.left);
        XQueryListValue r = (XQueryListValue)visitor.visit(ctx.right);

        l.addAll(r);
        return l;
    }
    public XQueryListValue evalSlash(@NotNull XqSlashContext ctx){
        XQueryListValue xq = (XQueryListValue)visitor.visit(ctx.xq());
        qc.pushContextElement(xq);
        XQueryListValue rp = (XQueryListValue)visitor.visit(ctx.rp());
        qc.popContextElement();
        return rp.unique();
    }
    public XQueryListValue evalTagname(@NotNull XqTagNameContext ctx) {
        XQueryListValue xq = (XQueryListValue)visitor.visit(ctx.xq());
        return new XQueryListValue(new XMLElement(ctx.tagName.getText(), xq));
    }

    public XQueryListValue evalFLWR(@NotNull XqFLWRContext ctx) {
        return null;
    }

    public XQueryListValue evalLet(@NotNull XqLetContext ctx) {
        return null;
    }
}
