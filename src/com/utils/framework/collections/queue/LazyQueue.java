package com.utils.framework.collections.queue;

import com.utils.framework.CollectionUtils;
import com.utils.framework.collections.iterator.ReverseIterator;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 3/1/13
 * Time: 7:02 PM
 */
public abstract class LazyQueue<T> extends AbstractQueue<T> {
    private ArrayList<T> elements = new ArrayList<T>();

    protected abstract List<T> loadData();

    @Override
    public Iterator<T> iterator() {
        return new ReverseIterator<T>(elements) {
            @Override
            public boolean hasNext() {
                while (!super.hasNext()) {
                    List<T> data = loadData();
                    if (data == null) {
                        return false;
                    }

                    CollectionUtils.addAllInReverseOrder(elements, data);
                }

                return true;
            }
        };
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean offer(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T poll() {
        while (elements.isEmpty()) {
            List<T> data = loadData();
            if (data == null) {
                return null;
            }

            CollectionUtils.addAllInReverseOrder(elements, data);
        }

        return elements.remove(elements.size() - 1);
    }

    @Override
    public T peek() {
        return elements.get(elements.size() - 1);
    }
}
