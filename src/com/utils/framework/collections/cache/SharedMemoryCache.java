package com.utils.framework.collections.cache;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * Date: 26.02.13
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class SharedMemoryCache implements SharedCache {
    protected static class Key implements SelfSizeOfProvider {
        Cache cache;
        Object key;
        int size;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key1 = (Key) o;

            if (cache != null ? !cache.equals(key1.cache) : key1.cache != null) return false;
            if (key != null ? !key.equals(key1.key) : key1.key != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = cache != null ? cache.hashCode() : 0;
            result = 31 * result + (key != null ? key.hashCode() : 0);
            return result;
        }

        @Override
        public int sizeOf() {
            return size;
        }
    }

    private CacheWithSizeOfProvider<Key, Object> cache;

    protected SharedMemoryCache(CacheWithSizeOfProvider<Key, Object> cache) {
        this.cache = cache;
        cache.setSizeProvider(new SizeProvider<Key, Object>() {
            @Override
            public int sizeOf(Key key, Object value) {
                return key.size;
            }
        });
    }

    public <K, V> Cache<K, V> createCache(final SizeProvider<K, V> sizeProvider) {
        return new Cache<K, V>() {
            private Key getInternalKey(K key) {
                Key internalKey = new Key();
                internalKey.cache = this;
                internalKey.key = key;
                return internalKey;
            }

            @Override
            public V get(K key) {
                Key internalKey = getInternalKey(key);
                return (V) cache.get(internalKey);
            }

            @Override
            public V put(K key, V value) {
                Key internalKey = getInternalKey(key);
                internalKey.size = sizeProvider.sizeOf(key, value);
                return (V) cache.put(internalKey, value);
            }

            @Override
            public boolean contains(K key) {
                Key internalKey = getInternalKey(key);
                return cache.contains(internalKey);
            }
        };
    }

    public <K, V> Cache<K, V> createCache() {
        return createCache(new DefaultSizeProvider<K, V>());
    }
}
