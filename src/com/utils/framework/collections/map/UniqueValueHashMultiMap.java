package com.utils.framework.collections.map;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by CM on 8/29/2014.
 */
public class UniqueValueHashMultiMap<K, V> extends SetValuesHashMultiMap<K, V> {
    private Set<V> valuesSet = createValuesSet();

    private Set<V> createValuesSet() {
        return new LinkedHashSet<>();
    }

    @Override
    public V put(K key, V value) throws ValueExistsException {
        if (valuesSet.contains(value)) {
            throw new ValueExistsException();
        }

        valuesSet.add(value);
        return super.put(key, value);
    }

    @Override
    public Collection<V> getAllValues() {
        return valuesSet;
    }

    @Override
    public boolean remove(K key, V value) {
        valuesSet.remove(value);
        return super.remove(key, value);
    }
}
