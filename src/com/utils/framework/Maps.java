package com.utils.framework;

import java.util.HashMap;

/**
 * Created by CM on 1/25/2015.
 */
public class Maps {
    public static <K, V> HashMap<K, V> hashMap(K key, V value) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }
}
