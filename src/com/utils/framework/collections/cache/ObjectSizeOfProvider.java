package com.utils.framework.collections.cache;

import com.utils.framework.memory.Memory;

/**
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 1:19 PM
 */
public class ObjectSizeOfProvider<K,V> implements SizeProvider<K,V>{
    private static final Memory MEMORY = Memory.getInstance();

    @Override
    public int sizeOf(K key, V value) {
        int keySize = MEMORY.sizeOf(key);
        int valueSize = MEMORY.sizeOf(value);
        return valueSize + keySize;
    }
}
