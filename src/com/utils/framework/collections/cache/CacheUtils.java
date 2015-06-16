package com.utils.framework.collections.cache;

/**
 * User: stikhonenko
 * Date: 2/25/13
 * Time: 2:58 PM
 */
public final class CacheUtils {
    public interface Getter<V> {
        V get() throws Exception;
    }

    public static <K, V> V saveGetAndPut(Cache<K, V> cache, K key, Getter<V> getter) throws Exception {
        V result = saveGet(cache, key);
        if (result == null) {
            try {
                result = getter.get();
            } catch (Exception e) {
                throw new Exception(e);
            }
            cache.put(key, result);
        }

        return result;
    }

    public static <K, V> V saveGet(Cache cache, K key) {
        try {
            return (V) cache.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static <K> SetCache<K> setCacheFromCache(final Cache<K, K> cache) {
        return new SetCache<K>() {
            @Override
            public K get(K key) {
                return cache.get(key);
            }

            @Override
            public K putOrGet(K key) {
                K result = cache.get(key);
                if (result != null) {
                    return result;
                }

                cache.put(key, key);

                return key;
            }

            @Override
            public boolean contains(K key) {
                return cache.contains(key);
            }
        };
    }
}
