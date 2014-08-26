package com.utils.framework;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 3/11/13
 * Time: 5:54 PM
 */
public interface ListItemComparator<T> {
    int compare(T a, T b, int indexA, int indexB);
}
