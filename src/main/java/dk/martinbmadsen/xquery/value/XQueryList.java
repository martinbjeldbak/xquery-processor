package dk.martinbmadsen.xquery.value;

import dk.martinbmadsen.xquery.xmltree.IXMLElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class XQueryList implements IXQueryValue, Iterable<IXMLElement>, Collection<IXMLElement>, List<IXMLElement> {
    private List<IXMLElement> values;

    public XQueryList() {
        this(10);
    }

    public XQueryList(int size) {
        this.values = new ArrayList<>(size);
    }

    public XQueryList(IXMLElement e) {
        this(1);
        values.add(e);
    }

    public XQueryList(List<IXMLElement> values) {
        this.values = values;
    }

    /**
     * Gets the unique elements (by ID (from discussion with prof 2015-04-28 after class) from this instance
     * in a new list
     * @return a new list with the unique elements from the current list's instance.
     */
    public XQueryList unique(){
        if (values == null)
            return null;
        XQueryList results = new XQueryList();

        values.stream().filter(e -> !containsRef(results, e)).forEach(results::add);
        return results;
    }

    private boolean containsRef(List<IXMLElement> list, IXMLElement elem) {
        for (IXMLElement x : list)
            if (!(x == null) && x.equalsRef(elem))
                return true;
        return false;
    }

    public XQueryFilter equalsId(XQueryList o) {
        for(IXMLElement x : this)
            for(IXMLElement y : o)
                if(x.equalsRef(y))
                    return XQueryFilter.trueValue();
        return XQueryFilter.falseValue();
    }

    public XQueryFilter equalsVal(XQueryList o) {
        for(IXMLElement x : this)
            for(IXMLElement y : o)
                if(x.equals(y))
                    return XQueryFilter.trueValue();
        return XQueryFilter.falseValue();
    }

    public XQueryFilter empty() {
        if(this.size() == 0)
            return XQueryFilter.trueValue();
        return XQueryFilter.falseValue();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof XQueryList) {
            XQueryList o = ((XQueryList) obj);
            return equalsVal(o) == XQueryFilter.trueValue();
        }

        return false;
    }

    @Override
    public String toString() {
        return values.toString();
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
    public Iterator<IXMLElement> iterator() {
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
    public boolean add(IXMLElement e) {
        return e != null && values.add(e);
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
        boolean result = false;
        for (IXMLElement x : c)
            result |= add(x);
        return result;
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
        if (values.size() > 0)
            return values.get(index);
        else
            return null;
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
