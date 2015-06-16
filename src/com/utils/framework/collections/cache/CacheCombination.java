package com.utils.framework.collections.cache;

/**
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 3:00 PM
 */
public class CacheCombination<K, V> implements Cache<K, V> {
    private Cache<K, V>[] caches;

    public CacheCombination(Cache<K, V>... caches) {
        this.caches = caches;
    }

    public Cache<K, V>[] getCaches() {
        return caches;
    }

    public void setCaches(Cache<K, V>[] caches) {
        this.caches = caches;
    }

    @Override
    public V get(K key) {
        for (Cache<K, V> cache : caches) {
            V value = cache.get(key);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    public V putIfAbsent(K key, V value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }

        V result = null;

        for (Cache<K, V> cache : caches) {
            if (cache.contains(key)) {
                if (result == null) {
                    result = cache.get(key);
                }
            } else {
                cache.put(key, value);
            }
        }

        return result;
    }

    @Override
    public V put(K key, V value) {
        for (Cache<K, V> cache : caches) {
            cache.put(key, value);
        }

        return null;
    }

    @Override
    public boolean contains(K key) {
        for (Cache<K, V> cache : caches) {
            if (cache.contains(key)) {
                return true;
            }
        }

        return false;
    }
}
