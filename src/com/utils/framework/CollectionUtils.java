package com.utils.framework;

import com.utils.framework.collections.TimSort;
import com.utils.framework.predicates.InstanceOfPredicate;

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

    public static <T> T removeLast(List<T> list) {
        int index = list.size() - 1;
        return list.remove(index);
    }

    public static  <T> List<Integer> getSortedListIndexingMap(final List<T> list, final Comparator<T> comparator){
        List<Integer> indexingMap = new ArrayList<Integer>();
        for(int i = 0; i < list.size(); i++){
            indexingMap.add(i);
        }

        Collections.sort(indexingMap, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer integer2) {
                T a = list.get(integer);
                T b = list.get(integer2);
                return comparator.compare(a,b);
            }
        });

        return indexingMap;
    }

    public static  <T extends Comparable> List<Integer> getSortedListIndexingMap(final List<T> list){
        return getSortedListIndexingMap(list, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return lhs.compareTo(rhs);
            }
        });
    }

    public static <T> void sortWithIndexesComparator(final List<T> list, final Comparator<Integer> comparator){
        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return comparator.compare(list.indexOf(lhs),list.indexOf(rhs));
            }
        });
    }

    public static Integer incrementAndGet(List<Integer> list, int index){
        Integer integer = list.get(index);
        return list.set(index, integer + 1);
    }

    public static <T extends Collection> int getGeneralSize(Collection<T> collections){
        int result = 0;
        for(T collection : collections){
            result += collection.size();
        }
        return result;
    }

    public static <T> int getGeneralSizeOfArrays(Collection<T[]> arrays){
        int result = 0;
        for(T[] array : arrays){
            result += array.length;
        }
        return result;
    }

    public static <T> List<T> mergeToSingleListSequentially(List<List<T>> lists){
        int size = getGeneralSize(lists);
        List<T> result = new ArrayList<T>(size);
        List<List<T>> listsClone = new ArrayList<List<T>>(lists);
        List<Integer> indexInLists = new ArrayList(Collections.nCopies(listsClone.size(),0));

        for(int i = 0; i < size;){
            int listIndex = i % listsClone.size();
            int indexInList = CollectionUtils.incrementAndGet(indexInLists, listIndex);
            List<T> list = listsClone.get(listIndex);
            if(list == null){
                throw new NullPointerException("list in lists should not be null");
            }

            if(indexInList >= list.size()){
                listsClone.remove(listIndex);
                indexInLists.remove(listIndex);
                continue;
            }

            T object = list.get(indexInList);
            result.add(object);

            i++;
        }

        return result;
    }

    public static <T> void sortUsingSeveralComparators(List<T> list, Comparator<T>... comparators){
        if(comparators.length == 0){
            throw new IllegalArgumentException();
        }

        Collections.sort(list, Comparators.comparatorCombination(comparators));
    }

    public static <T> void sortUsingListItemComparator(T[] list, ListItemComparator<T> comparator){
        TimSort.sort(list, comparator);
    }

    public static <T> void sortUsingListItemComparator(List<T> list, ListItemComparator<T> comparator){
        Object[] a = list.toArray();
        sortUsingListItemComparator(a, (ListItemComparator)comparator);
        ListIterator i = list.listIterator();
        for (int j=0; j<a.length; j++) {
            i.next();
            i.set(a[j]);
        }
    }

    public static interface PrioritiesProvider<T>{
        int getPriorityOf(T object, int index);
        int getPrioritiesCount();
    }

    public static <T> void sortByPriority(List<T> list, PrioritiesProvider<T> prioritiesProvider){
        int prioritiesCount = prioritiesProvider.getPrioritiesCount();
        int[][] resultIndexesByPriority = new int[prioritiesCount][list.size()];
        int[] indexesByPriorityLengths = new int[prioritiesCount];

        for(int i = 0; i < list.size(); i++){
            T object = list.get(i);
            int priority = prioritiesProvider.getPriorityOf(object, i);
            int[] arrayOfPriority = resultIndexesByPriority[priority];
            int index = indexesByPriorityLengths[priority]++;
            arrayOfPriority[index] = i;
        }

        Object[] clone = list.toArray();

        int listIndex = 0;
        for(int i = 0; i < prioritiesCount; i++){
            int[] arrayOfPriority = resultIndexesByPriority[i];
            int arrayOfPriorityLength = indexesByPriorityLengths[i];
            for(int j = 0; j < arrayOfPriorityLength; j++, listIndex++){
                int index = arrayOfPriority[j];
                T object = (T)clone[index];
                list.set(listIndex,object);
            }
        }
    }

    public static <T> void addAllInReverseOrder(Collection<T> destination, List<T> source){
        for(int i = source.size() - 1; i >= 0; i--){
            T object = source.get(i);
            destination.add(object);
        }
    }
}
