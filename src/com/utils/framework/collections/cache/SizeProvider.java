package com.utils.framework.collections.cache;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * Date: 26.02.13
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */
public interface SizeProvider<K, V> {
    int sizeOf(K key, V value);
}
