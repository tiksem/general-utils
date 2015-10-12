package com.utils.framework;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * User: stikhonenko
 * Date: 3/11/13
 * Time: 4:41 PM
 */
public class Comparators {
    public static <T> Comparator<T> comparatorCombination(final List<Comparator<T>> comparators) {
        return new Comparator<T>() {
            @Override
            public int compare(T a, T b) {
                for (Comparator<T> comparator : comparators) {
                    int compare = comparator.compare(a, b);
                    if (compare != 0) {
                        return compare;
                    }
                }

                return 0;
            }
        };
    }

    public static <T> Comparator<T> comparatorCombination(final Comparator<T>... comparators) {
        return comparatorCombination(Arrays.asList(comparators));
    }

    public static <T> Comparator<T> defaultComparator() {
        return new Comparator<T>() {
            @Override
            public int compare(T t, T t2) {
                return ((Comparable) t).compareTo((Comparable) t2);
            }
        };
    }

    public static <T> Comparator<T> reverseComparator(final Comparator<T> comparator) {
        return new Comparator<T>() {
            @Override
            public int compare(T t, T t2) {
                return -comparator.compare(t, t2);
            }
        };
    }

    public static <T, K extends Comparable<K>> Comparator<T> byKey(final KeyProvider<K, T> keyProvider) {
        return new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                K key1 = keyProvider.getKey(lhs);
                K key2 = keyProvider.getKey(rhs);
                return key1.compareTo(key2);
            }
        };
    }
}
