package com.utils.framework.collections.cache;

/**
 * Created with IntelliJ IDEA.
 *
 * Date: 26.02.13
 * Time: 22:11
 * To change this template use File | Settings | File Templates.
 */
public interface CacheWithSizeOf<K,V> extends Cache<K,V>{
    int sizeOf(K key, V value);
}
