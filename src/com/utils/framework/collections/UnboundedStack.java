package com.utils.framework.collections;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: Tikhonenko.S
 * Date: 09.07.14
 * Time: 22:03
 */
public class UnboundedStack<T> implements Stack<T> {
    private LinkedList<T> linkedList = new LinkedList<T>();

    @Override
    public void push(T value) {
        linkedList.add(value);
    }

    @Override
    public T pop() {
        if (!linkedList.isEmpty()) {
            return linkedList.removeLast();
        }

        return null;
    }

    @Override
    public T top() {
        if (!linkedList.isEmpty()) {
            return linkedList.getLast();
        }

        return null;
    }

    @Override
    public int size() {
        return linkedList.size();
    }

    @Override
    public void clear() {
        linkedList.clear();
    }
}
