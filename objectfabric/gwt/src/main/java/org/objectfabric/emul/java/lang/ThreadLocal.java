/**
 * This file is part of ObjectSync (http://objectsync.com).
 *
 * ObjectSync is licensed under the Apache License, Version 2.0, the terms
 * of which may be found at http://www.apache.org/licenses/LICENSE-2.0.html.
 * 
 * Copyright ObjectSync Inc.
 * 
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
 * WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package java.lang;

public class ThreadLocal<T> {

    private T _value;

    public T get() {
        return _value;
    }

    public void set(T value) {
        _value = value;
    }
}
