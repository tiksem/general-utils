package com.utils.framework;

import com.utils.framework.collections.ObjectAddressSet;
import com.utils.framework.collections.SetWithPredicates;
import com.utils.framework.collections.TimSort;
import com.utils.framework.collections.map.ListValuesMultiMap;
import com.utils.framework.collections.map.MultiMap;
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
    public static boolean contentEquals(Collection a, Collection b) {
        if (a == null) {
            return b == null;
        } else if (b == null) {
            return false;
        }

        if (a.size() != b.size()) {
            return false;
        }

        Iterator aIterator = a.iterator();
        Iterator bIterator = b.iterator();

        while (aIterator.hasNext()) {
            Object aItem = aIterator.next();
            Object bItem = bIterator.next();

            if (!aItem.equals(bItem)) {
                return false;
            }
        }

        return true;
    }

    public static <T> boolean contentEquals(Collection<T> a, Collection<T> b, Equals<T> equals) {
        if (a == null) {
            return b == null;
        } else if (b == null) {
            return false;
        }

        if (a.size() != b.size()) {
            return false;
        }

        Iterator<T> aIterator = a.iterator();
        Iterator<T> bIterator = b.iterator();

        while (aIterator.hasNext()) {
            T aItem = aIterator.next();
            T bItem = bIterator.next();

            if (!equals.equals(aItem, bItem)) {
                return false;
            }
        }

        return true;
    }

    public static double sum(Collection<? extends Number> collection) {
        double result = 0;
        for (Number number : collection) {
            result += number.doubleValue();
        }

        return result;
    }

    public static <T extends String> void removeAllIgnoreCase(Iterable<T> collection, final String... values) {
        for (String value : values) {
            removeIgnoreCase(collection, value);
        }
    }

    public static <T extends String> void removeAllIgnoreCase(Iterable<T> collection, final Iterable<String> values) {
        for (String value : values) {
            removeIgnoreCase(collection, value);
        }
    }

    public static <T extends String> void removeIgnoreCase(Iterable<T> collection, final String value) {
        remove(collection, new Predicate<T>() {
            @Override
            public boolean check(T item) {
                return item.equalsIgnoreCase(value);
            }
        });
    }

    public static <T> void remove(Iterable<T> collection, Predicate<T> predicate) {
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (predicate.check(item)) {
                iterator.remove();
                return;
            }
        }
    }

    public static <T> void removeAll(Iterable<T> collection, Predicate<T> predicate) {
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (predicate.check(item)) {
                iterator.remove();
            }
        }
    }

    public static <T> List<T> removeNonUniqueItems(final Collection<T> collection, final Equals<T> equals) {
        List<T> nonUnique = new ArrayList<T>();

        for (final T object : collection) {
            if (nonUnique.contains(object)) {
                continue;
            }

            List<T> list = CollectionUtils.findAll(collection, new Predicate<T>() {
                @Override
                public boolean check(T item) {
                    return item != object && equals.equals(item, object);
                }
            });

            if (!list.isEmpty()) {
                nonUnique.addAll(list);
                nonUnique.add(object);
            }
        }

        collection.removeAll(nonUnique);

        return nonUnique;
    }

    public static interface KeyProvider<K, V> {
        public K getKey(V value);
    }

    public static <K, V> void multiMapFromList(List<V> list,
                                               KeyProvider<K, V> keyProvider, MultiMap<K, V> out) {
        for (V object : list) {
            K key = keyProvider.getKey(object);
            out.put(key, object);
        }
    }

    public static <K, V> MultiMap<K, V> multiMapFromList(List<V> list, KeyProvider<K, V> keyProvider) {
        MultiMap<K, V> multiMap = new ListValuesMultiMap<K, V>();
        multiMapFromList(list, keyProvider, multiMap);
        return multiMap;
    }

    public static <K, V> void mapFromList(List<V> list,
                                          KeyProvider<K, V> keyProvider, Map<K, V> out) {
        for (V object : list) {
            K key = keyProvider.getKey(object);
            out.put(key, object);
        }
    }

    public static <K, V> Map<K, V> mapFromList(List<V> list,
                                               KeyProvider<K, V> keyProvider) {
        Map<K, V> map = new LinkedHashMap<K, V>();
        mapFromList(list, keyProvider, map);
        return map;
    }

    public static <T> List<T> getRemovedItems(Iterable<T> collection, Predicate<T> predicate) {
        List<T> result = new ArrayList<T>();

        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (predicate.check(item)) {
                iterator.remove();
                result.add(item);
            }
        }

        return result;
    }

    public static <T> void removeAllWithType(Collection<T> collection, Class<? extends T> aClass) {
        Predicate<T> predicate = new InstanceOfPredicate<T>(aClass);
        removeAll(collection, predicate);
    }

    public static <T> T find(Iterable<T> iterable, Predicate<T> predicate) {
        for (T object : iterable) {
            if (predicate.check(object)) {
                return object;
            }
        }

        return null;
    }

    public static <T> List<T> findAll(Iterable<T> iterable, Predicate<T> predicate) {
        List<T> result = new ArrayList<T>();
        for (T object : iterable) {
            if (predicate.check(object)) {
                result.add(object);
            }
        }

        return result;
    }

    public interface FindTransform<From, To> extends Transformer<From, To>, Predicate<From> {

    }

    public static <From, To> List<To> findAllAndTransform(Iterable<From> froms,
                                                          FindTransform<From, To> findTransform) {
        return transform(findAll(froms, findTransform), findTransform);
    }

    public static <T> List<T> findAll(Iterable<T> iterable, Predicate<T> predicate, int maxCount) {
        List<T> result = new ArrayList<T>(maxCount);
        for (T object : iterable) {
            if (predicate.check(object)) {
                result.add(object);
            }

            if (result.size() >= maxCount) {
                break;
            }
        }

        return result;
    }

    public static <T> List<T> findAll(Iterable<T> iterable, Class<? extends T> aClass) {
        Predicate<T> predicate = new InstanceOfPredicate<T>(aClass);
        return findAll(iterable, predicate);
    }

    public static <T> T getLast(List<T> list) {
        int index = list.size() - 1;
        return list.get(index);
    }

    public static <T> T setLast(List<T> list, T value) {
        int index = list.size() - 1;
        return list.set(index, value);
    }

    public static <T> T getFirstOrNull(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
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
                if (next == null) {
                    next = getNext();
                }

                return next != null;
            }

            @Override
            public T next() {
                T result = next;
                if (result == null) {
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
            Arrays.sort(indexes, new Comparator<Integer>() {
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

        Arrays.sort(indexes, new Comparator<Integer>() {
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

    public static <T> List<Integer> getSortedListIndexingMap(final List<T> list, final Comparator<T> comparator) {
        List<Integer> indexingMap = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            indexingMap.add(i);
        }

        Collections.sort(indexingMap, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer integer2) {
                T a = list.get(integer);
                T b = list.get(integer2);
                return comparator.compare(a, b);
            }
        });

        return indexingMap;
    }

    public static <T extends Comparable> List<Integer> getSortedListIndexingMap(final List<T> list) {
        return getSortedListIndexingMap(list, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return lhs.compareTo(rhs);
            }
        });
    }

    public static <T> void sortWithIndexesComparator(final List<T> list, final Comparator<Integer> comparator) {
        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return comparator.compare(list.indexOf(lhs), list.indexOf(rhs));
            }
        });
    }

    public static Integer incrementAndGet(List<Integer> list, int index) {
        Integer integer = list.get(index);
        return list.set(index, integer + 1);
    }

    public static <T> Integer changeValue(Map<T, Integer> map, T key, int value) {
        Integer integer = map.get(key);
        if (integer != null) {
            integer += value;
            map.put(key, integer);
        }

        return integer;
    }

    public static <T extends Collection> int getGeneralSize(Collection<T> collections) {
        int result = 0;
        for (T collection : collections) {
            result += collection.size();
        }
        return result;
    }

    public static <T> int getGeneralSizeOfArrays(Collection<T[]> arrays) {
        int result = 0;
        for (T[] array : arrays) {
            result += array.length;
        }
        return result;
    }

    public static <T> List<T> mergeToSingleListSequentially(List<List<T>> lists) {
        int size = getGeneralSize(lists);
        List<T> result = new ArrayList<T>(size);
        List<List<T>> listsClone = new ArrayList<List<T>>(lists);
        List<Integer> indexInLists = new ArrayList<Integer>(Collections.nCopies(listsClone.size(), 0));

        for (int i = 0; i < size; ) {
            int listIndex = i % listsClone.size();
            int indexInList = CollectionUtils.incrementAndGet(indexInLists, listIndex);
            List<T> list = listsClone.get(listIndex);
            if (list == null) {
                throw new NullPointerException("list in lists should not be null");
            }

            if (indexInList >= list.size()) {
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

    public static <T> void sortUsingSeveralComparators(List<T> list, Comparator<T>... comparators) {
        if (comparators.length == 0) {
            throw new IllegalArgumentException();
        }

        Collections.sort(list, Comparators.comparatorCombination(comparators));
    }

    public static <T> void sortUsingListItemComparator(T[] list, ListItemComparator<T> comparator) {
        TimSort.sort(list, comparator);
    }

    public static <T> void sortUsingListItemComparator(List<T> list, ListItemComparator<T> comparator) {
        Object[] a = list.toArray();
        sortUsingListItemComparator(a, (ListItemComparator) comparator);
        ListIterator i = list.listIterator();
        for (int j = 0; j < a.length; j++) {
            i.next();
            i.set(a[j]);
        }
    }

    public static interface PrioritiesProvider<T> {
        int getPriorityOf(T object, int index);

        int getPrioritiesCount();
    }

    public static <T> void sortByPriority(List<T> list, PrioritiesProvider<T> prioritiesProvider) {
        int prioritiesCount = prioritiesProvider.getPrioritiesCount();
        int[][] resultIndexesByPriority = new int[prioritiesCount][list.size()];
        int[] indexesByPriorityLengths = new int[prioritiesCount];

        for (int i = 0; i < list.size(); i++) {
            T object = list.get(i);
            int priority = prioritiesProvider.getPriorityOf(object, i);
            int[] arrayOfPriority = resultIndexesByPriority[priority];
            int index = indexesByPriorityLengths[priority]++;
            arrayOfPriority[index] = i;
        }

        Object[] clone = list.toArray();

        int listIndex = 0;
        for (int i = 0; i < prioritiesCount; i++) {
            int[] arrayOfPriority = resultIndexesByPriority[i];
            int arrayOfPriorityLength = indexesByPriorityLengths[i];
            for (int j = 0; j < arrayOfPriorityLength; j++, listIndex++) {
                int index = arrayOfPriority[j];
                T object = (T) clone[index];
                list.set(listIndex, object);
            }
        }
    }

    public static <T> void addAllInReverseOrder(Collection<T> destination, List<T> source) {
        for (int i = source.size() - 1; i >= 0; i--) {
            T object = source.get(i);
            destination.add(object);
        }
    }

    public static <T> void addAllInReverseOrder(List<T> destination, int index, List<T> source) {
        for (int i = source.size() - 1; i >= 0; i--) {
            T object = source.get(i);
            destination.add(index, object);
        }
    }

    public static <T> List<T> unique(List<T> list, final KeyProvider<Object, T> keyProvider) {
        return unique(list, new Equals<T>() {
            @Override
            public boolean equals(T a, T b) {
                return keyProvider.getKey(a).equals(keyProvider.getKey(b));
            }
        }, new HashCodeProvider<T>() {
            @Override
            public int getHashCodeOf(T object) {
                return keyProvider.getKey(object).hashCode();
            }
        });
    }

    public static <T> List<T> unique(List<T> list, Equals<T> equals, HashCodeProvider<T> hashCodeProvider) {
        Set<T> set = new SetWithPredicates<T>(new LinkedHashSet(), equals, hashCodeProvider);
        set.addAll(list);
        return new ArrayList<T>(set);
    }

    public static <T> List<T> unique(List<T> list, Equals<T> equals) {
        return unique(list, equals, null);
    }

    public static <T> List<T> unique(List<T> list) {
        Set<T> set = new LinkedHashSet<T>(list);
        return new ArrayList<T>(set);
    }

    public static <T> boolean replace(List<T> list, T value, T replacement) {
        int index = list.indexOf(value);
        if (index >= 0) {
            list.set(index, replacement);
            return true;
        }

        return false;
    }

    public interface Transformer<From, To> {
        To get(From from);
    }

    public static <From, To> List<To> transform(Iterable<From> from, Transformer<From, To> transformer) {
        List<To> result = new ArrayList<To>();
        for (From object : from) {
            result.add(transformer.get(object));
        }

        return result;
    }

    public static <From, To> List<To> transformNonCopy(List<From> from, Transformer<From, To> transformer) {
        final int size = from.size();
        return new AbstractList<To>() {
            @Override
            public To get(int location) {
                return transformer.get(from.get(location));
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    public static <From, To> List<To> transformUnique(Iterable<From> from, Transformer<From, To> transformer) {
        Set<To> result = new LinkedHashSet<To>();
        for (From object : from) {
            result.add(transformer.get(object));
        }

        return new ArrayList<To>(result);
    }

    public static <From, To> void transformAndAdd(Iterable<From> from, Collection<To> to,
                                                  Transformer<From, To> transformer) {
        for (From object : from) {
            to.add(transformer.get(object));
        }
    }

    public static <T> List<T> toList(Iterable<T> iterable, int maxCount) {
        List<T> result = new ArrayList<T>(maxCount);
        for (T object : iterable) {
            if (result.size() >= maxCount) {
                break;
            }

            result.add(object);
        }

        return result;
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        List<T> result = new ArrayList<T>();
        for (T object : iterable) {
            result.add(object);
        }

        return result;
    }

    public static <T> Iterable<T> asIterable(final Iterator<T> iterator) {
        return new Iterable<T>() {
            boolean called = false;

            @Override
            public Iterator<T> iterator() {
                if (called) {
                    throw new IllegalArgumentException("wrapped iterable twice iterator call");
                }
                called = true;

                return iterator;
            }
        };
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();

    }
}
