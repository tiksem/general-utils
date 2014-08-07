package com.utils.framework.collections;

import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 06.08.14
 * Time: 18:43
 */
public class ListSelectedItemPositionManager<T> extends AbstractListSelectedItemPositionManager<T> {
    private ListWithSelectedItem<T> listWithSelectedItem;

    public ListSelectedItemPositionManager(ListWithSelectedItem<T> listWithSelectedItem) {
        super(listWithSelectedItem);
        this.listWithSelectedItem = listWithSelectedItem;
    }

    @Override
    public int getCurrentItemPosition() {
        return listWithSelectedItem.getCurrentItemPosition();
    }

    @Override
    public void setCurrentItemPosition(int currentItemPosition) {
        listWithSelectedItem.setCurrentItemPosition(currentItemPosition);
    }

    @Override
    public List<T> getItems() {
        return listWithSelectedItem;
    }
}
