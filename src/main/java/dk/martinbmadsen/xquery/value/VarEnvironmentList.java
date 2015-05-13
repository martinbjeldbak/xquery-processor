package dk.martinbmadsen.xquery.value;

import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.util.*;

/**
 * Created by thalley on 5/13/15.
 */
public class VarEnvironmentList implements IXQueryValue, List<VarEnvironment> {
    public List<VarEnvironment> varEnvs;

    public VarEnvironmentList() {
        varEnvs = new ArrayList<>();
    }

    @Override
    public int size() {
        return varEnvs.size();
    }

    @Override
    public boolean isEmpty() {
        return varEnvs.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return varEnvs.contains(o);
    }

    @Override
    public Iterator<VarEnvironment> iterator() {
        return varEnvs.iterator();
    }

    @Override
    public Object[] toArray() {
        return varEnvs.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return varEnvs.toArray(ts);
    }

    @Override
    public boolean add(VarEnvironment varEnvironment) {
        return varEnvs.add(varEnvironment);
    }

    @Override
    public boolean remove(Object o) {
        return varEnvs.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return varEnvs.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends VarEnvironment> collection) {
        return varEnvs.addAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends VarEnvironment> collection) {
        return varEnvs.addAll(i, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return varEnvs.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return varEnvs.retainAll(collection);
    }

    @Override
    public void clear() {
        varEnvs.clear();

    }

    @Override
    public VarEnvironment get(int i) {
        return varEnvs.get(i);
    }

    @Override
    public VarEnvironment set(int i, VarEnvironment varEnvironment) {
        return varEnvs.set(i, varEnvironment);
    }

    @Override
    public void add(int i, VarEnvironment varEnvironment) {
        varEnvs.add(i, varEnvironment);

    }

    @Override
    public VarEnvironment remove(int i) {
        return varEnvs.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return varEnvs.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return varEnvs.lastIndexOf(o);
    }

    @Override
    public ListIterator<VarEnvironment> listIterator() {
        return varEnvs.listIterator();
    }

    @Override
    public ListIterator<VarEnvironment> listIterator(int i) {
        return varEnvs.listIterator(i);
    }

    @Override
    public List<VarEnvironment> subList(int i, int i1) {
        return varEnvs.subList(i, i1);
    }
}
