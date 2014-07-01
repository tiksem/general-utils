package com.utils.framework.patterns;

/**
 * User: Tikhonenko.S
 * Date: 26.12.13
 * Time: 15:24
 */
public interface StateChanger<T> {
    void changeState(T object);
}
