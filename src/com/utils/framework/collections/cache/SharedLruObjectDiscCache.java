package com.utils.framework.collections.cache;

/**
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 2:44 PM
 */
public class SharedLruObjectDiscCache extends SharedLruDiskCache {
    public SharedLruObjectDiscCache(int maxSize, CacheDirectoryPathGenerator cacheDirectoryPathGenerator) {
        super(new ObjectLruDiskCache<String, Object>(maxSize, cacheDirectoryPathGenerator));
    }
}
