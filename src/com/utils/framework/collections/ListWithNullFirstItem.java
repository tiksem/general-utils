package com.utils.framework.collections;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by CM on 8/30/2014.
 */
public class ListWithNullFirstItem<T> extends AbstractList<T> {
    private List<T> list;

    public ListWithNullFirstItem(List<T> list) {
        this.list = list;
    }

    @Override
    public T get(int location) {
        if(location == 0){
            return null;
        }

        return list.get(location - 1);
    }

    @Override
    public int size() {
        return list.size() + 1;
    }
}
