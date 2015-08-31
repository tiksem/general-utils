package com.utils.framework.collections;

/**
 * Created by CM on 8/30/2015.
 */
public abstract class AbstractStack<T> implements Stack<T> {
    @Override
    public boolean isEmpty() {
        return size() > 0;
    }

    @Override
    public void clear() {
        while (!isEmpty()) {
            pop();
        }
    }
}
