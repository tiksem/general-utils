package com.utils.framework.collections.cache;

/**
 *
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 2:19 PM
 */
public interface SharedCache{
    public <K,V> Cache<K,V> createCache(final SizeProvider<K, V> sizeProvider);
    public <K,V> Cache<K,V> createCache();
}
