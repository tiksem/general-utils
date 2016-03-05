package com.utils.framework.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by stykhonenko on 13.10.15.
 */
public class EmptyList<T> extends AbstractList<T> {
    @Override
    public T get(int index) {
        throw new AssertionError("WTF?");
    }

    @Override
    public int size() {
        return 0;
    }
}
