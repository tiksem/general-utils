package com.utils.framework;

/**
 * User: stikhonenko
 * Date: 2/28/13
 * Time: 7:13 PM
 */
public interface HashCodeProvider<T> {
    int getHashCodeOf(T object);
}
