
package java.util.concurrent.atomic;

public class AtomicInteger {

    private int _value;

    public AtomicInteger() {
    }

    public AtomicInteger(int value) {
        _value = value;
    }

    public int addAndGet(int delta) {
        _value += delta;
        return _value;
    }

    public boolean compareAndSet(int expect, int update) {
        if (_value != expect)
            return false;

        _value = update;
        return true;
    }

    public int decrementAndGet() {
        return --_value;
    }

    public int get() {
        return _value;
    }

    public int getAndIncrement() {
        return _value++;
    }

    public int incrementAndGet() {
        return ++_value;
    }

    public void set(int value) {
        _value = value;
    }
}
