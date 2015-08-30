package com.utils.framework;

/**
 * Created by CM on 8/29/2015.
 */
public interface KeyProvider<K, V> {
    public K getKey(V value);
}
