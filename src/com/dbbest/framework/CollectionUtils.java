package com.dbbest.framework;

import com.dbbest.framework.predicates.InstanceOfPredicate;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tikhonenko.S
 * Date: 05.11.13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class CollectionUtils {
    public static boolean contentEquals(Collection a, Collection b){
        if(a == null){
            return b == null;
        } else if(b == null) {
            return false;
        }

        if(a.size() != b.size()){
            return false;
        }

        Iterator aIterator = a.iterator();
        Iterator bIterator = b.iterator();

        while (aIterator.hasNext()) {
            Object aItem = aIterator.next();
            Object bItem = bIterator.next();

            if(!aItem.equals(bItem)){
                return false;
            }
        }

        return true;
    }

    public static <T> boolean contentEquals(Collection<T> a, Collection<T> b, Equals<T> equals){
        if(a == null){
            return b == null;
        } else if(b == null) {
            return false;
        }

        if(a.size() != b.size()){
            return false;
        }

        Iterator<T> aIterator = a.iterator();
        Iterator<T> bIterator = b.iterator();

        while (aIterator.hasNext()) {
            T aItem = aIterator.next();
            T bItem = bIterator.next();

            if(!equals.equals(aItem, bItem)){
                return false;
            }
        }

        return true;
    }

    public static double sum(Collection<? extends Number> collection){
        double result = 0;
        for(Number number : collection){
            result += number.doubleValue();
        }

        return result;
    }

    public static <T> void removeAll(Collection<T> collection, Predicate<T> predicate){
        Iterator<T> iterator = collection.iterator();
        while(iterator.hasNext()){
            T item = iterator.next();
            if(predicate.check(item)){
                iterator.remove();
            }
        }
    }

    public static <T> void removeAllWithType(Collection<T> collection, Class<? extends T> aClass){
        Predicate<T> predicate = new InstanceOfPredicate<T>(aClass);
        removeAll(collection, predicate);
    }

    public static <T> T find(Iterable<T> iterable, Predicate<T> predicate){
        for(T object : iterable){
            if(predicate.check(object)){
                return object;
            }
        }

        return null;
    }

    public static <T> List<T> findAll(Iterable<T> iterable, Predicate<T> predicate){
        List<T> result = new ArrayList<T>();
        for(T object : iterable){
            if(predicate.check(object)){
                result.add(object);
            }
        }

        return result;
    }

    public static <T> List<T> findAll(Iterable<T> iterable, Class<? extends T> aClass){
        Predicate<T> predicate = new InstanceOfPredicate<T>(aClass);
        return findAll(iterable, predicate);
    }

    public static <T> T getLast(List<T> list) {
        int index = list.size() - 1;
        return list.get(index);
    }

    public static <T> Iterator<T> predicateIterator(final Iterator<T> iterator, final Predicate<T> predicate) {
        return new Iterator<T>() {
            T next;

            private T getNext() {
                T result;
                do {
                    result = iterator.next();
                } while (result != null && !predicate.check(result));
                return result;
            }

            @Override
            public boolean hasNext() {
                if(next == null){
                    next = getNext();
                }

                return next != null;
            }

            @Override
            public T next() {
                T result = next;
                if(result == null){
                    result = getNext();
                }
                next = null;

                return result;
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    public static <T> Integer[] getSortedArrayIndexes(final T[] array, final Comparator<T> comparator) {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }

        if (comparator != null) {
            Arrays.sort(indexes, new Comparator<Integer>(){
                @Override
                public int compare(Integer aIndex, Integer bIndex) {
                    return comparator.compare(array[aIndex], array[bIndex]);
                }
            });
        }

        return indexes;
    }

    public static <T extends Comparable> Integer[] getSortedArrayIndexes(final T[] array) {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }

        Arrays.sort(indexes, new Comparator<Integer>(){
            @Override
            public int compare(Integer aIndex, Integer bIndex) {
                return array[aIndex].compareTo(array[bIndex]);
            }
        });

        return indexes;
    }
}
