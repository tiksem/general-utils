package com.utils.framework.collections.cache;

import java.util.WeakHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * Date: 06.11.12
 * Time: 22:34
 * To change this template use File | Settings | File Templates.
 */
public class StorageCache<K,V> extends LruCache<K,V> implements Cache<K,V> {
    //store weak references for getting objects, that are strong reachable, and so cannot be removed from cache
    private WeakHashMap<K,V> requestedForRemove = new WeakHashMap<K,V>();

    public StorageCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected V create(K key) {
        V value = requestedForRemove.get(key);
        if(value != null){
            put(key,value);
        }

        return value;
    }

    @Override
    protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
        if(evicted){
            requestedForRemove.put(key,newValue);
        }
    }
}
