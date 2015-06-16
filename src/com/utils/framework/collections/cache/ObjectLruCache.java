package com.utils.framework.collections.cache;

import com.utils.framework.memory.Memory;

/**
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 1:01 PM
 */
public class ObjectLruCache<K, V> extends LruCache<K, V> {
    private static final Memory MEMORY = Memory.getInstance();

    public ObjectLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public int sizeOf(K key, V value) {
        int keySize = MEMORY.sizeOf(key);
        int valueSize = MEMORY.sizeOf(value);
        return valueSize + keySize;
    }
}
