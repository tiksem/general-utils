package com.utils.framework.collections.cache;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * Date: 23.03.13
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
public interface SetCache<K> {
    K get(K key);

    K putOrGet(K key);

    boolean contains(K key);
}
