package com.utils.framework.collections.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 17:18
 */
public interface MultiMap<K, V> {
    boolean containsKey(K key);
    boolean containsValue(K key, V value);

    V put(K key, V value);
    void putAll(K key, Collection<V> values);
    boolean remove(K key, V value);
    boolean removeAll(K key);

    Collection<K> getKeys();
    Collection<V> getValues(K key);
    V getFirstValue(K key);
    Collection<V> getAllValues();

    Iterator<MultiMapEntry<K, V>> iterator();
    Iterator<Map.Entry<K, Collection<V>>> mapIterator();
    Map<K, Collection<V>> getMap();

    boolean isEmpty();
}
