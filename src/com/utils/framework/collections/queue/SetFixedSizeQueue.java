package com.utils.framework.collections.queue;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 07.01.13
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public class SetFixedSizeQueue<T>{
    private int maxSize;
    private LinkedHashSet<T> set = new LinkedHashSet<T>();

    public SetFixedSizeQueue(int maxSize) {
        this.maxSize = maxSize;
        if(maxSize <= 0){
            throw new IllegalArgumentException();
        }
    }

    public void add(T object){
        set.add(object);
        if(set.size() > maxSize){
            Iterator<T> iterator = set.iterator();
            T objectToRemove = iterator.next();
            iterator.remove();
            onEvicted(objectToRemove);
        }
    }

    public void remove(T object){
        set.remove(object);
    }

    protected void onEvicted(T object){

    }
}
