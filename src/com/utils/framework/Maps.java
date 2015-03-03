package com.utils.framework;

import com.utils.framework.strings.Strings;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by CM on 1/25/2015.
 */
public class Maps {
    public static <K, V> HashMap<K, V> hashMap(K key, V value) {
        HashMap<K, V> map = new HashMap<K, V>();
        map.put(key, value);
        return map;
    }

    public static <K, V> V get(Map<K, V> map, K key, V defaultValue) {
        V value = map.get(key);
        if(value == null){
            value = defaultValue;
        }

        return value;
    }

    public static <K, V> V getOrPut(Map<K, V> map, K key, V valueToPutIfNotExists) {
        V value = map.get(key);
        if(value == null){
            value = valueToPutIfNotExists;
            map.put(key, value);
        }

        return value;
    }

    public static <K, V> V getOrPut(ConcurrentHashMap<K, V> map, K key, V valueToPutIfNotExists) {
        V value = map.putIfAbsent(key, valueToPutIfNotExists);
        if(value == null){
            return valueToPutIfNotExists;
        }

        return value;
    }

    public static <K> int getInt(Map<K, String> map, K key, int defaultValue) {
        String strValue = map.get(key);
        if(strValue == null){
            return defaultValue;
        }

        try {
            return Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static <K> long getLong(Map<K, String> map, K key, long defaultValue) {
        String strValue = map.get(key);
        if(strValue == null){
            return defaultValue;
        }

        try {
            return Long.parseLong(strValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static <K> double getDouble(Map<K, String> map, K key, double defaultValue) {
        String strValue = map.get(key);
        if(strValue == null){
            return defaultValue;
        }

        try {
            return Double.parseDouble(strValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static <K> boolean getBoolean(Map<K, String> map, K key) {
        String strValue = map.get(key);
        return Strings.equalsIgnoreCase(strValue, "true");
    }

    public static <K, V> Map<K, Set<V>> toSetValuesMap(Map<K, List<V>> map) {
        Map<K, Set<V>> result = new HashMap<K, Set<V>>();
        for(Map.Entry<K, List<V>> entry : map.entrySet()){
            result.put(entry.getKey(), new LinkedHashSet<V>(entry.getValue()));
        }

        return result;
    }
}
