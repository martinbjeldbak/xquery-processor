package dk.martinbmadsen.xquery.value;

import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.util.*;

/**
 * Created by thalley on 5/13/15.
 */
public class VarEnvironmentList implements IXQueryValue{
    public List<VarEnvironment> varEnvs;

    public VarEnvironmentList() {
        varEnvs = new ArrayList<>();
    }

}
