import java.util.Iterator;

interface List<E> extends Iterable<E> {
    int size();
    boolean isEmpty();
    E get(int i) throws IndexOutOfBoundsException;
    E set(int i, E e) throws IndexOutOfBoundsException;
    void add(int i, E e) throws IndexOutOfBoundsException;
    E remove(int i) throws IndexOutOfBoundsException;
}

class DoublyLinkedList<E> {
    private static class Node<E> {
        private E element;               // reference to the element stored at this node
        private Node<E> prev;            // reference to the previous node in the list
        private Node<E> next;            // reference to the subsequent node in the list
        public Node(E e, Node<E> p, Node<E> n) {
            element = e;
            prev = p;
            next = n;
        }
        public E getElement() { return element; }
        public Node<E> getPrev() { return prev; }
        public Node<E> getNext() { return next; }
        public void setPrev(Node<E> p) { prev = p; }
        public void setNext(Node<E> n) { next = n; }
    }

    private Node<E> header;

    private Node<E> trailer;

    private int size = 0;

    public DoublyLinkedList()
    {
        header = new Node<>(null, null, null);
        trailer = new Node<>(null, header, null);
        header.setNext(trailer);
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public E first() {
        if (isEmpty()) return null;
        return header.getNext().getElement();
    }

    public E last() {
        if (isEmpty()) return null;
        return trailer.getPrev().getElement();
    }

    public void addFirst(E e) {
        addBetween(e, header, header.getNext());
    }

    public void addLast(E e) {
        addBetween(e, trailer.getPrev(), trailer);
    }

    public E removeFirst() {
        if (isEmpty()) return null;
        return remove(header.getNext());
    }

    public E removeLast() {
        if (isEmpty()) return null;
        return remove(trailer.getPrev());
    }

    private void addBetween(E e, Node<E> predecessor, Node<E> successor)
    {
        Node<E> newest = new Node<>(e, predecessor, successor);
        predecessor.setNext(newest);
        successor.setPrev(newest);
        size++;
    }

    private E remove(Node<E> node)
    {
        Node<E> predecessor = node.getPrev();
        Node<E> successor = node.getNext();
        predecessor.setNext(successor);
        successor.setPrev(predecessor);
        size--;
        return node.getElement();
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder("(");
        Node<E> walk = header.getNext();
        while (walk != trailer)
        {
            sb.append(walk.getElement());
            walk = walk.getNext();
            if (walk != trailer)
                sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}

public class LinkedList<E> implements List<E>
{
    private int size;

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public E get(int i) throws IndexOutOfBoundsException {
        return null;
    }

    public E set(int i, E e) throws IndexOutOfBoundsException {
        return null;
    }

    public void add(int i, E e) throws IndexOutOfBoundsException {

    }

    public E remove(int i) throws IndexOutOfBoundsException {
        return null;
    }

    public Iterator<E> iterator() { return null; }
}
