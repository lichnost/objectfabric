
package java.lang.ref;

/**
 * Platform classes allow other implementations to be used for ports like GWT and .NET.
 * The .NET specific implementations make it possible to remove Java components like
 * Reflection and Security from the ObjectSync dll.
 */
public class WeakReference<T> {

    private final T _t;

    public WeakReference(T t) {
        this(t, null);
    }

    @SuppressWarnings("unused")
    public WeakReference(T t, Object queue) {
        _t = t;
    }

    public final T get() {
        return _t;
    }

    public final void clear() {
    }
}
