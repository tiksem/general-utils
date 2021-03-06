package com.utils.framework;

import com.utils.framework.strings.Strings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static <K, V> V getOrThrow(Map<K, V> map, K key) {
        V value = map.get(key);
        if (value == null) {
            throw new IllegalStateException("Value of " + key + " doesn't exist");
        }

        return value;
    }

    public static <K, V> V getOrPut(Map<K, V> map, K key, V valueToPutIfNotExists) {
        V value = map.get(key);
        if (value == null) {
            value = valueToPutIfNotExists;
            map.put(key, value);
        }

        return value;
    }

    public static <K, V> V getOrPut(ConcurrentHashMap<K, V> map, K key, V valueToPutIfNotExists) {
        V value = map.putIfAbsent(key, valueToPutIfNotExists);
        if (value == null) {
            return valueToPutIfNotExists;
        }

        return value;
    }

    public static <K> int getInt(Map<K, String> map, K key, int defaultValue) {
        String strValue = map.get(key);
        if (strValue == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static <K> int getIntOrThrow(Map<K, String> map, K key) {
        String strValue = getOrThrow(map, key);
        try {
            return Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Error, value of " + key);
        }
    }

    public static <K> long getLong(Map<K, String> map, K key, long defaultValue) {
        String strValue = map.get(key);
        if (strValue == null) {
            return defaultValue;
        }

        try {
            return Long.parseLong(strValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static <K> long getLongOrThrow(Map<K, String> map, K key) {
        String strValue = getOrThrow(map, key);
        try {
            return Long.parseLong(strValue);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Error, value of " + key);
        }
    }

    public static <K> double getDouble(Map<K, String> map, K key, double defaultValue) {
        String strValue = map.get(key);
        if (strValue == null) {
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
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            result.put(entry.getKey(), new LinkedHashSet<V>(entry.getValue()));
        }

        return result;
    }

    public static <K> String[] getStringArray(Map<K, String> map, K key, String regExp) {
        String string = map.get(key);
        if (string == null) {
            return new String[0];
        }

        return string.split(regExp);
    }

    public static <K> String[] getStringArray(Map<K, String> map, K key) {
        return getStringArray(map, key, ", *");
    }

    public static Map<String, String> readProperties(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        return new LinkedHashMap<String, String>((Map) properties);
    }

    public static Map<String, String> readProperties(String filePath) throws IOException {
        return readProperties(new FileInputStream(filePath));
    }

    public static <K, V> Map<K, V> fromList(List<V> list, KeyProvider<K, V> keyProvider) {
        Map<K, V> map = new HashMap<>();
        for (V value : list) {
            K key = keyProvider.getKey(value);
            map.put(key, value);
        }

        return map;
    }
}
