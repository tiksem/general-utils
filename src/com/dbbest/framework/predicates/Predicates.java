package com.dbbest.framework.predicates;

import com.dbbest.framework.Predicate;

/**
 * User: Tikhonenko.S
 * Date: 16.12.13
 * Time: 18:13
 */
public class Predicates {
    public static <T> Predicate<T> not(final Predicate<T> predicate){
        return new Predicate<T>() {
            @Override
            public boolean check(T item) {
                return !predicate.check(item);
            }
        };
    }
}
