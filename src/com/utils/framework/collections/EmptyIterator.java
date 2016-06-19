package com.utils.framework.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by stikhonenko on 3/26/16.
 */
public class EmptyIterator<T> implements Iterator<T> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new NoSuchElementException();
    }
}
