package com.utils.framework;

/**
 * User: stikhonenko
 * Date: 2/28/13
 * Time: 7:31 PM
 */
public class DefaultEquals<T> implements Equals<T> {
    @Override
    public boolean equals(T a, T b) {
        return a.equals(b);
    }
}
