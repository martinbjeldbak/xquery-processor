package dk.martinbmadsen.xquery.error;

import org.antlr.v4.runtime.Token;

/**
 * Created by martin on 18/04/15.
 */
public class SyntaxError extends Error {
    Token token;

    public SyntaxError(String msg, Token token) {
        super(msg);
        this.token = token;
    }

    @Override
    public int getColumn() {
        return token.getCharPositionInLine();
    }

    @Override
    public int getLine() {
        return token.getLine();
    }
}
