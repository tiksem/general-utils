package com.utils.framework.collections.map;

import java.util.Map;

/**
 * Created by CM on 2/16/2015.
 */
public interface TwoKeysMap<K1, K2, V> {
    V get(K1 key1, K2 key2);

    V put(K1 key1, K2 key2, V value);

    V remove(K1 key1, K2 key2);

    Map<K2, V> getMap(K1 key);

    Map<K2, V> replaceMap(K1 key, Map<K2, V> replacement);
}
