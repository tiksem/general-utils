package com.utils.framework.collections;

import java.util.Arrays;

/**
 * User: Tikhonenko.S
 * Date: 12.12.13
 * Time: 13:34
 */
public class ArrayStack<T> extends AbstractStack<T> {
    private Object[] array;
    private int size = 0;

    public ArrayStack(Object[] array) {
        this.array = array;
    }

    public ArrayStack(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException();
        }

        array = new Object[capacity];
    }

    @Override
    public void push(T value) {
        if (size == capacity()) {
            throw new StackOverflowException();
        }

        array[size++] = value;
    }

    @Override
    public T pop() {
        T value = top();
        array[--size] = null;
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T top() {
        throwIfEmpty();
        return (T) array[size - 1];
    }

    private void throwIfEmpty() {
        if (size == 0) {
            throw new EmptyStackException();
        }
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return array.length;
    }

    @Override
    public void clear() {
        size = 0;
        Arrays.fill(array, null);
    }

    @Override
    public void replaceTop(T value) {
        throwIfEmpty();
        array[size - 1] = value;
    }
}
