package com.utils.framework.collections;

import com.utils.framework.Comparators;

import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 05.08.14
 * Time: 19:51
 */
public class DifferentlySortedList<T> extends AbstractList<T> {
    private static final Comparator DEFAULT_COMPARATOR = Comparators.defaultComparator();

    private Map<Comparator<T>, List<T>> sortedLists = new HashMap<Comparator<T>, List<T>>();
    private List<T> currentSortedList;

    public DifferentlySortedList(Collection<T> items) {
        List<T> list = new ArrayList<T>(items);
        sortedLists.put(DEFAULT_COMPARATOR, list);
        currentSortedList = list;
    }

    public List<T> getOriginalList() {
        return Collections.unmodifiableList(getInternalOriginalList());
    }

    private List<T> getInternalOriginalList() {
        return sortedLists.get(DEFAULT_COMPARATOR);
    }

    public List<T> getListBySortedComparator(Comparator<T> comparator) {
        List<T> list = sortedLists.get(comparator);
        if(list == null){
            list = new ArrayList<T>(getInternalOriginalList());
            sortedLists.put(comparator, list);
        }

        return list;
    }

    public void setCurrentSortComparator(Comparator<T> sortComparator) {
        currentSortedList = getListBySortedComparator(sortComparator);
    }

    @Override
    public T get(int location) {
        return currentSortedList.get(location);
    }

    @Override
    public int size() {
        return currentSortedList.size();
    }
}
