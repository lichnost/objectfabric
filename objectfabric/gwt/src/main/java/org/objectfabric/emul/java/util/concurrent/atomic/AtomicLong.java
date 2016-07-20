
package java.util.concurrent.atomic;

public class AtomicLong {

    private long _value;

    public long addAndGet(long delta) {
        _value += delta;
        return _value;
    }

    public boolean compareAndSet(long expect, long update) {
        if (_value != expect)
            return false;

        _value = update;
        return true;
    }

    public long decrementAndGet() {
        return --_value;
    }

    public long get() {
        return _value;
    }

    public long incrementAndGet() {
        return ++_value;
    }

    public void set(long value) {
        _value = value;
    }
}
