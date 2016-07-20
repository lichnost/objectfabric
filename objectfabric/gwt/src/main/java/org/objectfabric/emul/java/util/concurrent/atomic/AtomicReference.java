
package java.util.concurrent.atomic;

public class AtomicReference<T> {

    private T _value;

    public T get() {
        return _value;
    }

    public void set(T value) {
        _value = value;
    }

    public boolean compareAndSet(T expect, T update) {
        if (_value != expect)
            return false;

        _value = update;
        return true;
    }
}
