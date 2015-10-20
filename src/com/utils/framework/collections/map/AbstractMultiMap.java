package com.utils.framework.collections.map;

import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 19:21
 */
public abstract class AbstractMultiMap<K, V> implements MultiMap<K, V>, Iterable<MultiMapEntry<K, V>> {
    private Map<K, Collection<V>> map;

    protected AbstractMultiMap() {
        map = createMap();
    }

    @Override
    public boolean containsKey(K key) {
        Collection<V> values = getValues(key);
        return values != null && !values.isEmpty();
    }

    @Override
    public boolean containsValue(K key, V value) {
        Collection<V> values = getValues(key);
        return values != null && values.contains(value);
    }

    @Override
    public V put(K key, V value) {
        Collection<V> values = getValues(key);
        if (values == null) {
            values = createValuesCollection();
            putValuesCollection(key, values);
        }

        if (values.add(value)) {
            return null;
        } else {
            return value;
        }
    }

    @Override
    public void putAll(K key, Collection<V> values) {
        for (V value : values) {
            put(key, value);
        }
    }

    @Override
    public boolean remove(K key, V value) {
        Collection<V> values = getValues(key);
        return values != null && values.remove(value);
    }

    @Override
    public Collection<V> getAllValues() {
        ArrayList<V> result = new ArrayList<V>();
        for (MultiMapEntry<K, V> entry : this) {
            result.add(entry.value);
        }

        return result;
    }

    protected abstract Collection<V> createValuesCollection();

    protected abstract Map<K, Collection<V>> createMap();

    protected void putValuesCollection(K key, Collection<V> values) {
        map.put(key, values);
    }

    @Override
    public boolean removeAll(K key) {
        return map.remove(key) != null;
    }

    @Override
    public Collection<K> getKeys() {
        return map.keySet();
    }

    @Override
    public Collection<V> getValues(K key) {
        return map.get(key);
    }

    private class MapIterator<K, V> implements Iterator<MultiMapEntry<K, V>> {
        Iterator iterator = map.entrySet().iterator();
        Iterator<V> valueIterator;
        K key;

        @Override
        public boolean hasNext() {
            if (iterator.hasNext()) {
                return true;
            }

            if (valueIterator != null) {
                while (!valueIterator.hasNext()) {
                    if (iterator.hasNext()) {
                        updateIterator();
                    } else {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }

        private void updateIterator() {
            Map.Entry<K, Collection<V>> entry = (Map.Entry<K, Collection<V>>) iterator.next();
            valueIterator = entry.getValue().iterator();
            key = entry.getKey();
        }

        @Override
        public MultiMapEntry<K, V> next() {
            while (valueIterator == null || !valueIterator.hasNext()) {
                if (iterator.hasNext()) {
                    updateIterator();
                } else {
                    throw new NoSuchElementException();
                }
            }

            return new MultiMapEntry<K, V>(key, valueIterator.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<MultiMapEntry<K, V>> iterator() {
        return new MapIterator<K, V>();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public V getFirstValue(K key) {
        Collection<V> values = getValues(key);
        if (values == null || values.isEmpty()) {
            return null;
        }

        return values.iterator().next();
    }

    @Override
    public Iterator<Map.Entry<K, Collection<V>>> mapIterator() {
        return map.entrySet().iterator();
    }

    @Override
    public Map<K, Collection<V>> getMap() {
        return map;
    }

    @Override
    public void removeAll(MultiMap<K, V> multiMap) {
        for (MultiMapEntry<K, V> next : multiMap) {
            remove(next.key, next.value);
        }
    }

    @Override
    public void clear() {
        map.clear();
    }
}
