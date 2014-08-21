package com.utils.framework.collections.map;

import java.util.Collection;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 19:19
 */
public final class MultiMapEntry<K, V> {
    public K key;
    public V value;

    public MultiMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
