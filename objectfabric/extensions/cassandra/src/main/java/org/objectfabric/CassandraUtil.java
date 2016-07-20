
package org.objectfabric;

import java.nio.ByteBuffer;

abstract class CassandraUtil {

    static byte[] byteArray(ByteBuffer buffer) {
        return buffer.array();
    }

    static ByteBuffer byteBuffer(byte[] bytes) {
        return ByteBuffer.wrap(bytes);
    }
}
