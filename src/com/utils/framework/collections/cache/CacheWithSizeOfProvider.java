package com.utils.framework.collections.cache;

/**
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 2:28 PM
 */
public abstract class CacheWithSizeOfProvider<K, V> implements CacheWithSizeOf<K, V> {
    private SizeProvider<K, V> sizeProvider;

    @Override
    public int sizeOf(K key, V value) {
        if (sizeProvider != null) {
            return sizeProvider.sizeOf(key, value);
        } else {
            return 1;
        }
    }

    public SizeProvider<K, V> getSizeProvider() {
        return sizeProvider;
    }

    public void setSizeProvider(SizeProvider<K, V> sizeProvider) {
        this.sizeProvider = sizeProvider;
    }
}
