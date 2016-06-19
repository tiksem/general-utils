package com.utils.framework.collections.map;

import com.utils.framework.collections.EmptySet;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by stikhonenko on 3/26/16.
 */
public class EmptyMap<K,V> extends AbstractMap<K,V> {
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EmptySet<>();
    }
}
