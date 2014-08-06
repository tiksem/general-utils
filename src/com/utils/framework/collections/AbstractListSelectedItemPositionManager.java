package com.utils.framework.collections;

import com.utils.framework.Cancelable;

import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 06.08.14
 * Time: 18:37
 */
public abstract class AbstractListSelectedItemPositionManager<T> implements SelectedItemPositionManager<T> {
    private List<T> list;

    protected AbstractListSelectedItemPositionManager(List<T> list) {
        this.list = list;
    }

    @Override
    public T getCurrentSelectedItem() {
        return list.get(getCurrentItemPosition());
    }

    @Override
    public T selectPrev() {
        if(getCurrentItemPosition() < 0){
            throw new IllegalStateException("currentItemPosition < 0");
        }

        setCurrentItemPosition(getCurrentItemPosition() - 1);
        if(getCurrentItemPosition() < 0){
            setCurrentItemPosition(list.size() - 1);
        }

        return getCurrentSelectedItem();
    }

    @Override
    public T selectNext() {
        if(getCurrentItemPosition() < 0){
            throw new IllegalStateException("currentItemPosition < 0");
        }

        setCurrentItemPosition(getCurrentItemPosition() + 1);
        if(getCurrentItemPosition() >= list.size()){
            setCurrentItemPosition(0);
        }

        return getCurrentSelectedItem();
    }

    @Override
    public boolean canSelectNext() {
        return getCurrentItemPosition() >= 0;
    }

    @Override
    public boolean isNextAvailable() {
        return canSelectNext();
    }

    @Override
    public boolean canSelectPrev() {
        return getCurrentItemPosition() >= 0;
    }

    @Override
    public boolean isPrevAvailable() {
        return canSelectPrev();
    }

    @Override
    public Cancelable selectNextWhenAvailable(OnAvailable<T> onNextAvailable) {
        if(getCurrentItemPosition() < 0){
            throw new IllegalStateException();
        }

        onNextAvailable.onItemAvailable(selectNext());

        return new Cancelable() {
            @Override
            public void cancel() {
            }
        };
    }

    @Override
    public Cancelable selectPrevWhenAvailable(OnAvailable<T> onPrevAvailable) {
        if(getCurrentItemPosition() < 0){
            throw new IllegalStateException();
        }

        onPrevAvailable.onItemAvailable(selectPrev());

        return new Cancelable() {
            @Override
            public void cancel() {
            }
        };
    }
}
