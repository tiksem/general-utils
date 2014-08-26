package com.utils.framework.collections.iterator;

import java.util.Iterator;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 3/11/13
 * Time: 3:56 PM
 */
public class AbstractIterator<T> implements Iterator<T> {
    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T next() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
