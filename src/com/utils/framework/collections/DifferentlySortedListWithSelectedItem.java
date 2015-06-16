package com.utils.framework.collections;

import com.utils.framework.Cancelable;

import java.util.Collection;
import java.util.Comparator;

/**
 * User: Tikhonenko.S
 * Date: 05.08.14
 * Time: 20:17
 */
public class DifferentlySortedListWithSelectedItem<T> extends DifferentlySortedList<T>
        implements ListWithSelectedItem<T> {
    private int currentItemPosition = -1;

    public DifferentlySortedListWithSelectedItem(Collection<T> items) {
        super(items);
    }

    @Override
    public void setCurrentSortComparator(Comparator<T> sortComparator) {
        T currentItem = null;
        if (currentItemPosition >= 0) {
            currentItem = get(currentItemPosition);
        }

        super.setCurrentSortComparator(sortComparator);

        if (currentItem != null) {
            currentItemPosition = indexOf(currentItem);
        }
    }

    @Override
    public int getCurrentItemPosition() {
        return currentItemPosition;
    }

    @Override
    public void setCurrentItemPosition(int currentItemPosition) {
        this.currentItemPosition = currentItemPosition;
    }
}
