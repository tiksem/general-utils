package com.utils.framework.collections.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CM on 2/16/2015.
 */
public class TwoKeysHashMap<K1, K2, V> extends AbstractTwoKeysMap<K1, K2, V> {
    @Override
    protected Map<K1, Map<K2, V>> createRootMap() {
        return new HashMap<K1, Map<K2, V>>();
    }

    @Override
    protected Map<K2, V> createInnerMap() {
        return new HashMap<K2, V>();
    }
}
