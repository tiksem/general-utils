package com.utils.framework.collections;

/**
 * User: Tikhonenko.S
 * Date: 12.12.13
 * Time: 13:33
 */
public interface Stack<T> {
    void push(T value);

    T pop();

    T top();

    int size();

    void clear();
}
