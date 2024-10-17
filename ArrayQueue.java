import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

interface Queue<E> {
    int size();
    boolean isEmpty();
    void enqueue(E e);
    E first();
    E dequeue();
}

public class ArrayQueue<E> implements Queue<E>, Iterable<E> {
    private class BinaryDirectionalIterator<E> implements TwoDirectionalIterator<E>
    {
        int j = 0;

        public boolean hasNext() { return j < size(); }

        public E next()
        {
            if (j == size()) throw new NoSuchElementException();
            return (E) data[ (f+j++) % data.length ];
        }

        public boolean hasPrevious() { return j==0; }

        public E previous() throws IllegalStateException
        {
            if( !hasPrevious() ) throw new IllegalStateException();
            return (E) data[ (f+j-1) % data.length ];
        }
    }

    public static final int CAPACITY = 1000;
    private E[] data;
    private int f = 0;
    private int sz = 0;
    public ArrayQueue() {this(CAPACITY);}

    public ArrayQueue(int capacity) {
        data = (E[]) new Object[capacity];
    }

    public int size() { return sz; }

    public boolean isEmpty() { return (sz == 0); }

    public void enqueue(E e) throws IllegalStateException
    {
        if (sz == data.length) throw new IllegalStateException("Queue is full");
        int avail = (f + sz) % data.length;
        data[avail] = e;
        sz++;
    }

    public E first() {
        if (isEmpty()) return null;
        return data[f];
    }

    public E dequeue() {
        if (isEmpty()) return null;

        E answer = data[f];
        data[f] = null;
        f = (f + 1) % data.length;
        sz--;
        return answer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        int k = f;
        for (int j=0; j < sz; j++)
        {
            if (j > 0)
                sb.append(", ");
            sb.append(data[k]);
            k = (k + 1) % data.length;
        }
        sb.append(")");
        return sb.toString();
    }

    public TwoDirectionalIterator<E> iterator()
    {
        return new BinaryDirectionalIterator<>();
    }

    public static void main(String[] args)
    {
        ArrayQueue<Integer> hovsep = new ArrayQueue<>();

        for (int i = 0; i < 15; i++) {
            hovsep.enqueue(i*i);
        }

        for(Integer integer : hovsep)
        {
            System.out.println(integer);
        }
    }
}
