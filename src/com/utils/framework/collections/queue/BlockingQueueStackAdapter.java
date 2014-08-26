package com.utils.framework.collections.queue;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 13.01.13
 * Time: 14:07
 * To change this template use File | Settings | File Templates.
 */
public class BlockingQueueStackAdapter<T> extends LinkedBlockingDeque<T> {
    public BlockingQueueStackAdapter(int capacity) {
        super(capacity);
    }

    public BlockingQueueStackAdapter(Collection<? extends T> c) {
        super(c);
    }

    public BlockingQueueStackAdapter() {

    }

    @Override
    public void put(T object) throws InterruptedException {
        putFirst(object);
    }

    @Override
    public boolean add(T object){
        addFirst(object);
        return true;
    }

    @Override
    public boolean offer(T object) {
        return offerFirst(object);
    }

    @Override
    public boolean offer(T object, long timeout, TimeUnit unit) throws InterruptedException {
        return offerFirst(object, timeout, unit);
    }
}
