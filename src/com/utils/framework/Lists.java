package com.utils.framework;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by CM on 9/11/2015.
 */
public class Lists {
    public static <T> T getLast(List<T> list) {
        int index = list.size() - 1;
        return list.get(index);
    }

    public static <T> T getLast(List<T> list, T defaultValue) {
        if (list.isEmpty()) {
            return defaultValue;
        } else {
            return getLast(list);
        }
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

    public static <T> T removeLast(List<T> list) {
        int index = list.size() - 1;
        return list.remove(index);
    }

    public static <T> ArrayList<T> arrayListWithNulls(int size) {
        ArrayList<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(null);
        }

        return list;
    }

    public static <T> List<T> concat(List<T> a, List<T> b) {
        ArrayList<T> result = new ArrayList<>(a.size() + b.size());
        result.addAll(a);
        result.addAll(b);
        return result;
    }

    public static <T> T getRandomItem(Random random, List<T> list) {
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    public static <T> List<T> listWithAddingElementToFront(final List<T> list, final T element) {
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                if (index == 0) {
                    return element;
                }

                return list.get(index - 1);
            }

            @Override
            public int size() {
                return list.size() + 1;
            }
        };
    }

    public static <T> List<T> listWithAddingElementToEnd(final List<T> list, final T element) {
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                if (index == list.size()) {
                    return element;
                }

                return list.get(index);
            }

            @Override
            public int size() {
                return list.size() + 1;
            }
        };
    }

    public static <T> List<T> fromIterable(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T t : iterable) {
            list.add(t);
        }

        return list;
    }

    public static <T> List<T> withoutNulls(T... items) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (item != null) {
                result.add(item);
            }
        }

        return result;
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> List<T> repeatedValues(final T value, final int size) {
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                return value;
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    public static <T> List<List<T>> separateIntoListsWithSize(int size, List<T> original) {
        List<List<T>> result = new ArrayList<>();
        List<T> currentRow = null;
        for (int i = 0; i < original.size(); i++) {
            if (i % size == 0) {
                if (currentRow != null) {
                    result.add(currentRow);
                }

                currentRow = new ArrayList<>();
            }

            T item = original.get(i);
            currentRow.add(item);
        }
        result.add(currentRow);
        return result;
    }

    // [a, b)
    public static <T> List<T> removeRangeGetRemovedItems(List<T> list, int a, int b) {
        List<T> subList = list.subList(a, b);
        List<T> removed = new ArrayList<T>(subList);
        subList.clear();
        return removed;
    }

    public static int size(List<?> list) {
        return list != null ? list.size() : 0;
    }
}
