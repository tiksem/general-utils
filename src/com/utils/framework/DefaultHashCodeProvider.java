package com.utils.framework;

/**
 * User: stikhonenko
 * Date: 2/28/13
 * Time: 7:32 PM
 */
public class DefaultHashCodeProvider<T> implements HashCodeProvider<T> {
    @Override
    public int getHashCodeOf(T object) {
        return object.hashCode();
    }
}
