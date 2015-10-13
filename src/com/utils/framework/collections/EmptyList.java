package com.utils.framework.collections;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by stykhonenko on 13.10.15.
 */
public class EmptyList<T> extends ArrayList<T> {
    @Override
    public boolean add(T object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }
}
