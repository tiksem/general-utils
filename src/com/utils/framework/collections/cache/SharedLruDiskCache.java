package com.utils.framework.collections.cache;

/**
 *
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 1:32 PM
 */
public abstract class SharedLruDiskCache implements SharedCache{
    private BaseLruDiskCache<String,Object> diskCache;

    protected SharedLruDiskCache(BaseLruDiskCache<String, Object> diskCache) {
        this.diskCache = diskCache;
    }

    @Override
    public <K, V> Cache<K, V> createCache(SizeProvider<K, V> sizeProvider) {
        // ignore
        return createCache();
    }

    @Override
    public <K, V> Cache<K, V> createCache() {
        return new Cache<K, V>() {
            String thisHashCode = String.valueOf(System.identityHashCode(this));

            final String getKey(K key){
                return thisHashCode + key.toString();
            }

            @Override
            public V get(K key) {
                String keyAsString = getKey(key);
                return (V)diskCache.get(keyAsString);
            }

            @Override
            public V put(K key, V value) {
                String keyAsString = getKey(key);
                diskCache.put(keyAsString, value);
                return null;
            }

            @Override
            public boolean contains(K key) {
                String keyAsString = getKey(key);
                return diskCache.contains(keyAsString);
            }
        };
    }
}
