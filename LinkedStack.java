import java.util.Iterator;
import java.util.NoSuchElementException;

interface Stack<E> {
    int size();
    boolean isEmpty();
    void push(E e);
    E top();
    E pop();
}

class SinglyLinkedList<E> implements Cloneable {
    static class Node<E> {

        private final E element;
        private Node<E> next;

        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        public Node(Node<E> n) {
            element = n.getElement();
            next = n.getNext();
        }

        public E getElement() { return element; }

        public Node<E> getNext() { return next; }

        public void setNext(Node<E> n) { next = n; }
    }

    Node<E> head = null;
    private Node<E> tail = null;
    int size = 0;
    public SinglyLinkedList() { }
    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public E first() {
        if (isEmpty()) return null;
        return head.getElement();
    }

    public E last() {
        if (isEmpty()) return null;
        return tail.getElement();
    }

    public void addFirst(E e) {
        head = new Node<>(e, head);
        if (size == 0)
            tail = head;
        size++;
    }

    public void addLast(E e) {
        Node<E> newest = new Node<>(e, null);
        if (isEmpty())
            head = newest;
        else
            tail.setNext(newest);
        tail = newest;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) return null;
        E answer = head.getElement();
        head = head.getNext();
        size--;
        if (size == 0)
            tail = null;
        return answer;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        SinglyLinkedList other = (SinglyLinkedList) o;
        if (size != other.size) return false;
        Node walkA = head;
        Node walkB = other.head;
        while (walkA != null) {
            if (!walkA.getElement().equals(walkB.getElement())) return false;
            walkA = walkA.getNext();
            walkB = walkB.getNext();
        }
        return true;
    }

    public SinglyLinkedList<E> clone() throws CloneNotSupportedException
    {
        SinglyLinkedList<E> other = (SinglyLinkedList<E>) super.clone();
        if (size > 0) {
            other.head = new Node<>(head.getElement(), null);
            Node<E> walk = head.getNext();
            Node<E> otherTail = other.head;
            while (walk != null) {
                Node<E> newest = new Node<>(walk.getElement(), null);
                otherTail.setNext(newest);
                otherTail = newest;
                walk = walk.getNext();
            }
        }
        return other;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Node<E> walk = head;
        while (walk != null) {
            sb.append(walk.getElement());
            if (walk != tail)
                sb.append(", ");
            walk = walk.getNext();
        }
        sb.append(")");
        return sb.toString();
    }
}

public class LinkedStack<E> implements Stack<E>, Iterable<E>
{
    private class LinkedStackIterator implements Iterator<E> {
        private SinglyLinkedList.Node<E> current;
        private SinglyLinkedList.Node<E> previous;

        public LinkedStackIterator ()
        {
            current = values.head;
            previous = null;
        }

        // O(1), it just checks if the pointer isn't null.
        public boolean hasNext() { return current != null; }

        // O(1), because it performs a constant number of primitive operations
        public E next()
        {
            if ( !hasNext() ) throw new NoSuchElementException("no such element");

            E element = current.getElement();

            previous = current;
            current = current.getNext();

            return element;
        }

        /**
         * Best case: O(1), if the previous is the top node,
         * Worst case: O(n), if it must go through the entire list
         *
         * Time complexity: O(n).
         */
        public void remove() {
            if (previous == null) throw new IllegalStateException("nothing to remove");

            if ( previous == top() ) { pop(); }
            else
            {
                SinglyLinkedList.Node<E> temp = values.head;

                while ( temp.getNext() != previous)
                    temp = temp.getNext();

                temp.setNext(previous.getNext());
                previous.setNext(null);
                values.size--;
            }
        }
    }

    SinglyLinkedList<E> values;

    public LinkedStack ()
    {
        values = new SinglyLinkedList<>();
    }

    public int size() { return values.size(); }
    public boolean isEmpty() { return values.isEmpty(); }
    public void push(E e) { values.addFirst(e); }
    public E top() { return values.first(); }
    public E pop() { return values.removeFirst(); }
    public Iterator<E> iterator() { return new LinkedStackIterator(); }

    public static void main(String[] args) {

        LinkedStack<Integer> stack = new LinkedStack<>();
        for (int i = 0; i < 10; i++) stack.push(5*i+3);

        Iterator<Integer> iterator = stack.iterator();
        int something = 0;

        while (iterator.hasNext())
        {
            iterator.next();
            something++;
            if(something == 4) iterator.remove();
        }

        for (Integer e : stack) System.out.print(e + " ");
    }
}
