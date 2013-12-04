package com.dbbest.framework;

import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 04.12.13
 * Time: 14:33
 */
public class Sets {
    public static <T extends Object> Set<T> intersection(Collection<Set<T>> sets){
        if(sets.size() == 0){
            return new HashSet<T>();
        }

        Iterator<Set<T>> iterator = sets.iterator();
        Set<T> intersection = new HashSet<T>(iterator.next());
        while (iterator.hasNext()) {
            intersection.retainAll(iterator.next());
        }

        return intersection;
    }

    public static <T extends Object> Set<T> intersection(List<Set<T>> sets){
        return intersection((Collection<Set<T>>)sets);
    }
}
