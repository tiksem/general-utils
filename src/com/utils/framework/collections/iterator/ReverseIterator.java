package com.utils.framework.collections.iterator;

import java.util.List;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 3/11/13
 * Time: 3:57 PM
 */
public class ReverseIterator<T> extends AbstractIterator<T> {
    private int index;
    private List<T> list;

    public ReverseIterator(List<T> list){
        setList(list);
    }

    @Override
    public boolean hasNext() {
        return index >= 0;
    }

    @Override
    public T next() {
        return list.get(index--);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
        index = list.size() - 1;
    }
}
