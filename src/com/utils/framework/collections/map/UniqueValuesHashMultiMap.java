package com.utils.framework.collections.map;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 20:00
 */
public class UniqueValuesHashMultiMap<K, V> extends AbstractMultiMap<K, V> {
    @Override
    protected Collection<V> createValuesCollection() {
        return new LinkedHashSet<V>();
    }

    @Override
    protected Map<K, Collection<V>> createMap() {
        return new LinkedHashMap<K, Collection<V>>();
    }
}
