import java.util.Iterator;
import java.util.NoSuchElementException;

interface Position<E> {
    E getElement() throws IllegalStateException;
}

interface PositionalList<E> extends Iterable<E>
{
    int size();
    boolean isEmpty();
    Position<E> last();
    Position<E> before(Position<E> p) throws IllegalArgumentException;
    Position<E> after(Position<E> p) throws IllegalArgumentException;
    Position<E> addFirst(E e);
    Position<E> addLast(E e);
    Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException;
    Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException;
    E set(Position<E> p, E e) throws IllegalArgumentException;
    E remove(Position<E> p) throws IllegalArgumentException;
    Iterator<E> iterator();
    Iterable<Position<E>> positions();
}

class LinkedPositionalList<E> implements PositionalList<E> {

    private static class Node<E> implements Position<E> {

        private E element;
        private Node<E> prev;
        private Node<E> next;

        public Node(E e, Node<E> p, Node<E> n) {
            element = e;
            prev = p;
            next = n;
        }

        public E getElement() throws IllegalStateException {
            if (next == null)
                throw new IllegalStateException("Position no longer valid");
            return element;
        }

        public Node<E> getPrev() { return prev; }

        public Node<E> getNext() { return next; }

        public void setElement(E e) { element = e; }

        public void setPrev(Node<E> p) { prev = p; }

        public void setNext(Node<E> n) { next = n; }
    }

    private Node<E> header;

    private Node<E> trailer;

    private int size = 0;

    public LinkedPositionalList() {
        header = new Node<>(null, null, null);
        trailer = new Node<>(null, header, null);
        header.setNext(trailer);
    }

    private Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if ( !(p instanceof Node) ) throw new IllegalArgumentException("Invalid p");
        Node<E> node = (Node<E>) p;
        if (node.getNext() == null)
            throw new IllegalArgumentException("p is no longer in the list");
        return node;
    }
    private Position<E> position(Node<E> node) {
        if (node == header || node == trailer)
            return null;
        return node;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public Position<E> first() { return position(header.getNext()); }

    public Position<E> last() { return position(trailer.getPrev()); }

    public Position<E> before(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return position(node.getPrev());
    }

    public Position<E> after(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return position(node.getNext());
    }

    private Position<E> addBetween(E e, Node<E> pred, Node<E> succ)
    {
        Node<E> newest = new Node<>(e, pred, succ);
        pred.setNext(newest);
        succ.setPrev(newest);
        size++;
        return newest;
    }

    public Position<E> addFirst(E e) { return addBetween(e, header, header.getNext()); }

    public Position<E> addLast(E e) { return addBetween(e, trailer.getPrev(), trailer); }

    public Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException
    {
        Node<E> node = validate(p);
        return addBetween(e, node.getPrev(), node);
    }

    public Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException
    {
        Node<E> node = validate(p);
        return addBetween(e, node, node.getNext());
    }

    public E set(Position<E> p, E e) throws IllegalArgumentException
    {
        Node<E> node = validate(p);
        E answer = node.getElement();
        node.setElement(e);
        return answer;
    }

    public E remove(Position<E> p) throws IllegalArgumentException
    {
        Node<E> node = validate(p);
        Node<E> predecessor = node.getPrev();
        Node<E> successor = node.getNext();
        predecessor.setNext(successor);
        successor.setPrev(predecessor);
        size--;
        E answer = node.getElement();
        node.setElement(null);
        node.setNext(null);
        node.setPrev(null);
        return answer;
    }

    private class PositionIterator implements Iterator<Position<E>> {

        private Position<E> cursor = first();

        private Position<E> recent = null;

        public boolean hasNext() { return (cursor != null);  }

        public Position<E> next() throws NoSuchElementException
        {
            if (cursor == null) throw new NoSuchElementException("nothing left");
            recent = cursor;
            cursor = after(cursor);
            return recent;
        }

        public void remove() throws IllegalStateException
        {
            if (recent == null) throw new IllegalStateException("nothing to remove");
            LinkedPositionalList.this.remove(recent);
            recent = null;
        }
    }

    private class PositionIterable implements Iterable<Position<E>> {
        public Iterator<Position<E>> iterator() { return new PositionIterator(); }
    }

    public Iterable<Position<E>> positions() {
        return new PositionIterable();
    }

    private class ElementIterator implements Iterator<E>
    {
        Iterator<Position<E>> posIterator = new PositionIterator();
        public boolean hasNext() { return posIterator.hasNext(); }
        public E next() { return posIterator.next().getElement(); }
        public void remove() { posIterator.remove(); }
    }

    public Iterator<E> iterator() { return new ElementIterator(); }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("(");
        Node<E> walk = header.getNext();
        while (walk != trailer) {
            sb.append(walk.getElement());
            walk = walk.getNext();
            if (walk != trailer)
                sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}


public class PositionalMergeSort<E extends Comparable<E>>
{
    public void mergeSort(LinkedPositionalList<E> S) {
        int n = S.size();
        if (n < 2) return;
        int mid = n/2;

        LinkedPositionalList<E> S1 = new LinkedPositionalList<>();
        LinkedPositionalList<E> S2 = new LinkedPositionalList<>();

        Position<E> cursor = S.first();

        for (int i = 0; i < mid; i++)
        {
            S1.addLast(cursor.getElement());
            cursor = S.after(cursor);
        }

        for (int i = mid; i < n; i++)
        {
            S2.addLast(cursor.getElement());
            cursor = S.after(cursor);
        }

        mergeSort(S1);
        mergeSort(S2);
        merge(S1, S2, S);
    }
    public void merge (LinkedPositionalList<E> S1, LinkedPositionalList<E> S2, LinkedPositionalList<E> S)
    {
        Position<E> i = S1.first();
        Position<E> j = S2.first();

        while (!S.isEmpty())
            S.remove(S.last());

        // Maybe I should use iterators, but "If it works don't touch it"
        while(i != null || j != null)
        {
            if (j == null || i != null && i.getElement().compareTo(j.getElement()) < 0
            ) {
                S.addLast(i.getElement());
                i = S1.after(i);
            }
            else{
                S.addLast(j.getElement());
                j = S2.after(j);
            }
        }
    }

    public static void main(String[] args) {
        LinkedPositionalList<Integer> list = new LinkedPositionalList<>();

        // Absolutely random numbers
        for (int i = 70; i > 30; i-=2)
            list.addLast(i);

        PositionalMergeSort<Integer> sorter = new PositionalMergeSort<>();

        sorter.mergeSort(list);

        System.out.println(list);
    }
}
