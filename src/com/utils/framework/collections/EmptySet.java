package com.utils.framework.collections;

import java.util.AbstractSet;
import java.util.Iterator;

/**
 * Created by stikhonenko on 3/26/16.
 */
public class EmptySet<T> extends AbstractSet<T> {
    @Override
    public Iterator<T> iterator() {
        return new EmptyIterator<>();
    }

    @Override
    public int size() {
        return 0;
    }
}
