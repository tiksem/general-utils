package com.utils.framework.collections;

import java.util.Arrays;

/**
 * User: Tikhonenko.S
 * Date: 12.12.13
 * Time: 13:34
 */
public class ArrayStack<T> implements Stack<T>{
    private Object[] array;
    private int size = 0;

    public ArrayStack(Object[] array) {
        this.array = array;
    }

    public ArrayStack(int capacity){
        if(capacity < 0){
            throw new IllegalArgumentException();
        }

        array = new Object[capacity];
    }

    @Override
    public void push(T value) {
        if(size == capacity()){
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
        if(size == 0){
            throw new EmptyStackException();
        }

        return (T)array[size - 1];
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity(){
        return array.length;
    }

    @Override
    public void clear() {
        size = 0;
        Arrays.fill(array, null);
    }
}
