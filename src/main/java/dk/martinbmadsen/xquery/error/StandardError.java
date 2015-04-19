package dk.martinbmadsen.xquery.error;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class StandardError extends Error {
    private ParserRuleContext ctx;

    public StandardError(String msg, ParserRuleContext ctx) {
        super(msg);
        this.ctx = ctx;
    }

    @Override
    public int getColumn() {
        return -1;
    }

    @Override
    public int getLine() {
        return -1;
    }
}
