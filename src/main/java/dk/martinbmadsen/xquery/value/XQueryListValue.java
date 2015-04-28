package dk.martinbmadsen.xquery.value;

import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class XQueryListValue implements IXQueryValue, Iterable<IXMLElement>, Collection<IXMLElement>, List<IXMLElement> {
    private List<IXMLElement> values;

    public XQueryListValue() {
        this(10);
    }

    public XQueryListValue(int size) {
        this.values = new ArrayList<>(size);
    }

    public XQueryListValue(IXMLElement e) {
        this(1);
        values.add(e);
    }

    public XQueryListValue(List<IXMLElement> values) {
        this.values = values;
    }

    /**
     * Gets the unique elements (by ID (from discussion with prof 2015-04-28 after class) from this instance
     * in a new list
     * @return a new list with the unique elements from the current list's instance.
     */
    public XQueryListValue unique(){
        if (values == null)
            return null;
        XQueryListValue results = new XQueryListValue();

        values.stream().filter(e -> !containsRef(results, e)).forEach(results::add);
        return results;
    }

    private boolean containsRef(List<IXMLElement> list, IXMLElement elem) {
        for (IXMLElement x : list)
            if (x.equalsRef(elem))
                return true;
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof XQueryListValue) {
            XQueryListValue l = ((XQueryListValue) obj);
            return values.equals(l.values);
        }

        return super.equals(obj);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return values.contains(o);
    }

    @Override
    public Iterator iterator() {
        return values.iterator();
    }

    @Override
    public Object[] toArray() {
        return values.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return values.toArray(a);
    }

    @Override
    public boolean add(IXMLElement ixmlElement) {
        return values.add(ixmlElement);
    }

    @Override
    public boolean remove(Object o) {
        return values.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return values.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends IXMLElement> c) {
        return values.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends IXMLElement> c) {
        return values.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return values.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super IXMLElement> filter) {
        return values.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return values.retainAll(c);
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public IXMLElement get(int index) {
        return values.get(index);
    }

    @Override
    public IXMLElement set(int index, IXMLElement element) {
        return values.set(index, element);
    }

    @Override
    public void add(int index, IXMLElement element) {
        values.add(index, element);
    }

    @Override
    public IXMLElement remove(int index) {
        return values.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return values.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return values.lastIndexOf(o);
    }

    @Override
    public ListIterator<IXMLElement> listIterator() {
        return values.listIterator();
    }

    @Override
    public ListIterator<IXMLElement> listIterator(int index) {
        return values.listIterator(index);
    }

    @Override
    public List<IXMLElement> subList(int fromIndex, int toIndex) {
        return values.subList(fromIndex, toIndex);
    }

    @Override
    public void forEach(Consumer action) {
        values.forEach(action);
    }

    @Override
    public Spliterator spliterator() {
        return values.spliterator();
    }

    @Override
    public Stream<IXMLElement> stream() {
        return values.stream();
    }

    @Override
    public Stream<IXMLElement> parallelStream() {
        return values.parallelStream();
    }
}
