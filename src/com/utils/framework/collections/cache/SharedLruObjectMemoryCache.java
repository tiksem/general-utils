package com.utils.framework.collections.cache;

/**
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 1:21 PM
 */
public class SharedLruObjectMemoryCache extends SharedLruMemoryCache {
    public SharedLruObjectMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public <K, V> Cache<K, V> createCache() {
        return createCache(new ObjectSizeOfProvider<K, V>());
    }
}
