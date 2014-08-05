package com.utils.framework.collections.cache;

/**
 *
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 2:52 PM
 */
public class EmptySharedCache implements SharedCache{
    @Override
    public <K, V> Cache<K, V> createCache(SizeProvider<K, V> sizeProvider) {
        return new EmptyCache<K, V>();
    }

    @Override
    public <K, V> Cache<K, V> createCache() {
        return new EmptyCache<K, V>();
    }
}
