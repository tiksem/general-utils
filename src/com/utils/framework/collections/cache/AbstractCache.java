package com.utils.framework.collections.cache;

/**
 * Created by CM on 9/10/2015.
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {
    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }
}
