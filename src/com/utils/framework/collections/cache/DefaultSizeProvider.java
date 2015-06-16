package com.utils.framework.collections.cache;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * Date: 26.02.13
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSizeProvider<K, V> implements SizeProvider<K, V> {
    @Override
    public int sizeOf(K key, V value) {
        return 1;
    }
}
