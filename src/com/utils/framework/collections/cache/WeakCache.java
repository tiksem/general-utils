package com.utils.framework.collections.cache;

import java.util.WeakHashMap;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * Date: 23.03.13
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public class WeakCache<K, V> implements Cache<K, V> {
    private WeakHashMap<K, V> weakHashMap = new WeakHashMap<K, V>();

    @Override
    public V get(K key) {
        return weakHashMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        return weakHashMap.put(key, value);
    }

    @Override
    public boolean contains(K key) {
        return weakHashMap.containsKey(key);
    }

    @Override
    public void clear() {
        weakHashMap.clear();
    }
}
