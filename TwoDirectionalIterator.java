import java.util.Iterator;

public interface TwoDirectionalIterator<E> extends Iterator<E>
{
    E previous() throws NullPointerException;
}
