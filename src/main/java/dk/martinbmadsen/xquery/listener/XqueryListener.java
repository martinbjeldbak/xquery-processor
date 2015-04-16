package dk.martinbmadsen.xquery.listener;

import dk.martinbmadsen.xquery.parser.XQueryBaseListener;
import dk.martinbmadsen.xquery.parser.XQueryParser;

/**
 * Created by martin on 16/04/15.
 */
public class XQueryListener extends XQueryBaseListener {
    @Override
    public void enterAp(XQueryParser.ApContext ctx) {
        System.out.println(ctx.rp().getText());
    }
}
