package com.utils.framework.collections;

import com.utils.framework.Cancelable;

/**
 * User: Tikhonenko.S
 * Date: 06.08.14
 * Time: 18:36
 */
public interface SelectedItemPositionManager<T> {
    public interface OnAvailable<T> {
        void onItemAvailable(T item);
    }

    public int getCurrentItemPosition();
    public void setCurrentItemPosition(int currentItemPosition);
    public T getCurrentSelectedItem();
    public T selectNext();
    public T selectPrev();
    public boolean canSelectNext();
    public boolean isNextAvailable();
    public boolean canSelectPrev();
    public boolean isPrevAvailable();
    public Cancelable selectNextWhenAvailable(OnAvailable<T> onNextAvailable);
    public Cancelable selectPrevWhenAvailable(OnAvailable<T> onPrevAvailable);
}
