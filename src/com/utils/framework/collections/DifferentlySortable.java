package com.utils.framework.collections;

import java.util.Comparator;

/**
 * User: Tikhonenko.S
 * Date: 07.08.14
 * Time: 20:20
 */
public interface DifferentlySortable<T> {
    public void setCurrentSortComparator(Comparator<T> sortComparator);
}
