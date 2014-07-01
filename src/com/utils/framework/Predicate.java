package com.utils.framework;

/**
 * User: Tikhonenko.S
 * Date: 04.12.13
 * Time: 16:21
 */
public interface Predicate<T> {
    boolean check(T item);
}
