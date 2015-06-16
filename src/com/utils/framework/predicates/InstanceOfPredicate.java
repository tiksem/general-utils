package com.utils.framework.predicates;

import com.utils.framework.Predicate;

/**
 * User: Tikhonenko.S
 * Date: 16.12.13
 * Time: 14:23
 */
public class InstanceOfPredicate<T> implements Predicate<T> {
    private Class<? extends T> aClass;

    public InstanceOfPredicate(Class<? extends T> aClass) {
        this.aClass = aClass;
    }

    @Override
    public boolean check(T item) {
        return aClass.isAssignableFrom(item.getClass());
    }
}
