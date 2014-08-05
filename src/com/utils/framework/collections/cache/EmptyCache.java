package com.utils.framework.collections.cache;

/**
 *
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 1:26 PM
 */
public class EmptyCache<K,V> implements CacheWithSizeOf<K,V>{
    @Override
    public int sizeOf(K key, V value) {
        return 0;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public boolean contains(K key) {
        return false;
    }
}
