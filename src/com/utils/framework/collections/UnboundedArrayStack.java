package com.utils.framework.collections;

import com.utils.framework.ArrayUtils;

import java.util.Arrays;

/**
 * Created by CM on 8/31/2015.
 */
public class UnboundedArrayStack<T> extends AbstractStack<T> {
    private static final int MIN_CAPACITY_INCREMENT = 12;
    private static final float CAPACITY_FACTOR = 0.5f;

    private Object[] array;
    private int size;

    private void expandCapacity() {
        array = ArrayUtils.expandCapacity(array, CAPACITY_FACTOR, MIN_CAPACITY_INCREMENT);
    }

    public UnboundedArrayStack(int capacity) {
        array = new Object[capacity];
    }

    @Override
    public void push(T value) {
        if (size == array.length) {
            expandCapacity();
        }

        array[size++] = value;
    }

    private void throwIfEmpty() {
        if (size == 0) {
            throw new EmptyStackException();
        }
    }

    @Override
    public T pop() {
        throwIfEmpty();
        T value = (T) array[--size];
        array[size] = null;
        return value;
    }

    @Override
    public T top() {
        throwIfEmpty();
        return (T) array[size - 1];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        Arrays.fill(array, 0, size, null);
    }

    @Override
    public void replaceTop(T value) {
        throwIfEmpty();
        array[size - 1] = value;
    }
}
