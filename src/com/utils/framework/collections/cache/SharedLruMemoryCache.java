package com.utils.framework.collections.cache;

/**
 *
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 1:31 PM
 */
public class SharedLruMemoryCache extends SharedMemoryCache {
    public SharedLruMemoryCache(int maxSize) {
        super(new LruCache<Key, Object>(maxSize));
    }
}
