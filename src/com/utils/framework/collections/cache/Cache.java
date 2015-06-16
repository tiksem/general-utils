package com.utils.framework.collections.cache;

/**
 * User: stikhonenko
 * Date: 2/25/13
 * Time: 2:32 PM
 */
public interface Cache<K, V> {
    V get(K key);

    V put(K key, V value);

    boolean contains(K key);
}
