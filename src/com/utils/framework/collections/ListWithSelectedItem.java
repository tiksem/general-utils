package com.utils.framework.collections;

import com.utils.framework.Cancelable;

import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 05.08.14
 * Time: 20:24
 */
public interface ListWithSelectedItem<T> extends List<T> {
    public int getCurrentItemPosition();
    public void setCurrentItemPosition(int currentItemPosition);
}
