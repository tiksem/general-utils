package com.utils.framework.patterns;

/**
 * User: Tikhonenko.S
 * Date: 26.12.13
 * Time: 15:00
 */
public interface StateProvider<T, State> {
    State getState(T object);
    void restoreState(T object, State state);
}
