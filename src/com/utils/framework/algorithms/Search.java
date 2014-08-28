package com.utils.framework.algorithms;

import com.utils.framework.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 02.01.13
 * Time: 3:21
 * To change this template use File | Settings | File Templates.
 */
public final class Search {
    public static <T> List<T> filter(List<T> data, String filter, int maxCount){
        List<T> result = new ArrayList<T>(data.size());
        int listIndex = 0;
        int count = 0;

        for(T object : data){
            if(count >= maxCount){
                return result;
            }

            String objectAsString = object.toString();
            if(objectAsString.contains(filter)){
                result.add(object);
                count++;
            }
        }

        return result;
    }

    public static <T> List<T> filter(List<T> data, Predicate<T> filter){
        List<T> result = new ArrayList<T>(data.size());

        for(T object : data){
            if(filter.check(object)){
                result.add(object);
            }
        }

        return result;
    }

    public static <T> List filter(List<T> data, String filter){
        return filter(data, filter, Integer.MAX_VALUE);
    }

    public static <T> T find(Iterable<T> iterable, Predicate<T> predicate){
        for(T i : iterable){
            if(predicate.check(i)){
                return i;
            }
        }
        return null;
    }
}
