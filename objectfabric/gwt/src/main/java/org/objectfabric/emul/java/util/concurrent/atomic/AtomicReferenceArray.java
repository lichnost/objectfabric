
package java.util.concurrent.atomic;

public class AtomicReferenceArray<T> {

    private T[] _array;

    @SuppressWarnings("unchecked")
    public AtomicReferenceArray(int length) {
        _array = (T[]) new Object[length];
    }

    public int length() {
        return _array.length;
    }

    public T get(int index) {
        return _array[index];
    }

    public void set(int index, T value) {
        _array[index] = value;
    }

    public boolean compareAndSet(int index, T expect, T update) {
        if (_array[index] != expect)
            return false;

        _array[index] = update;
        return true;
    }
}
