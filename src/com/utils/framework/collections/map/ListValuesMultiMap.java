package com.utils.framework.collections.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by CM on 8/30/2014.
 */
public class ListValuesMultiMap<K, V> extends AbstractMultiMap<K, V> {
    @Override
    protected Collection<V> createValuesCollection() {
        return new ArrayList<V>();
    }

    @Override
    protected Map<K, Collection<V>> createMap() {
        return new LinkedHashMap<K, Collection<V>>();
    }
}
