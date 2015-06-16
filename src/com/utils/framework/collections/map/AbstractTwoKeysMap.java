package com.utils.framework.collections.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by CM on 2/16/2015.
 */
public abstract class AbstractTwoKeysMap<K1, K2, V> implements TwoKeysMap<K1, K2, V> {
    private Map<K1, Map<K2, V>> map;

    public AbstractTwoKeysMap() {
        map = createRootMap();
    }

    protected abstract Map<K1, Map<K2, V>> createRootMap();

    protected abstract Map<K2, V> createInnerMap();

    @Override
    public V get(K1 key1, K2 key2) {
        Map<K2, V> innerMap = getMap(key1);
        if (innerMap == null) {
            return null;
        }

        return innerMap.get(key2);
    }

    @Override
    public V put(K1 key1, K2 key2, V value) {
        Map<K2, V> innerMap = getMap(key1);
        if (innerMap == null) {
            innerMap = createInnerMap();
            map.put(key1, innerMap);
        }

        return innerMap.put(key2, value);
    }

    @Override
    public Map<K2, V> getMap(K1 key) {
        return map.get(key);
    }

    @Override
    public Map<K2, V> replaceMap(K1 key, Map<K2, V> replacement) {
        return map.put(key, replacement);
    }

    @Override
    public V remove(K1 key1, K2 key2) {
        Map<K2, V> innerMap = getMap(key1);
        if (innerMap == null) {
            return null;
        }

        return innerMap.remove(key2);
    }
}
