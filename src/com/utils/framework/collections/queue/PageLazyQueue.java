package com.utils.framework.collections.queue;

import java.util.List;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 3/1/13
 * Time: 7:52 PM
 */
public abstract class PageLazyQueue<T> extends LazyQueue<T> {
    private int loadedPagesCount;

    public int getLoadedPagesCount() {
        return loadedPagesCount;
    }

    @Override
    protected final List<T> loadData() {
        return loadData(loadedPagesCount++);
    }

    protected abstract List<T> loadData(int pageNumber);
}
