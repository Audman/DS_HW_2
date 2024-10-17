import java.util.NoSuchElementException;

interface Queue<E> {
    int size();
    boolean isEmpty();
    void enqueue(E e);
    E first();
    E dequeue();
}

public class ArrayQueue<E> implements Queue<E>, Iterable<E>
{
    private class BinaryDirectionalIterator<E> implements TwoDirectionalIterator<E>
    {
        int j = 0;

        // O(1)
        public boolean hasNext() { return j < size(); }

        // O(1), no loops
        public E next()
        {
            if (j == size()) throw new NoSuchElementException();
            return (E) values[ (f+j++) % values.length ];
        }

        // O(1)
        public boolean hasPrevious() { return j==0; }

        // O(1), no loops
        public E previous() throws IllegalStateException
        {
            if( !hasPrevious() ) throw new IllegalStateException();
            return (E) values[ (f+j-1) % values.length ];
        }
    }

    public static final int CAPACITY = 1000;
    private E[] values;
    private int f = 0;
    private int sz = 0;
    public ArrayQueue() {this(CAPACITY);}

    public ArrayQueue(int capacity) {
        values = (E[]) new Object[capacity];
    }

    public int size() { return sz; }

    public boolean isEmpty() { return (sz == 0); }

    public void enqueue(E e) throws IllegalStateException
    {
        if (sz == values.length) throw new IllegalStateException("Queue is full");
        int avail = (f + sz) % values.length;
        values[avail] = e;
        sz++;
    }

    public E first() {
        if (isEmpty()) return null;
        return values[f];
    }

    public E dequeue() {
        if (isEmpty()) return null;

        E answer = values[f];
        values[f] = null;
        f = (f + 1) % values.length;
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
            sb.append(values[k]);
            k = (k + 1) % values.length;
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
        ArrayQueue<Integer> queueue = new ArrayQueue<>();

        for (int i = 0; i < 15; i++)
            queueue.enqueue(i*i);

        for(Integer integer : queueue)
            System.out.println(integer);
    }
}
