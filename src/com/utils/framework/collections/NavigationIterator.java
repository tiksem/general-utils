package com.utils.framework.collections;

import com.utils.framework.OnError;
import com.utils.framework.collections.iterator.AbstractIterator;

import java.util.*;

/**
 * Created by CM on 1/25/2015.
 */
public abstract class NavigationIterator<T> extends AbstractIterator<T> implements NavigationEntity<T> {
    private int pageNumber;
    private int loadedItemsCount = 0;
    private boolean allDataLoaded;
    private Iterator<T> iterator = Collections.emptyIterator();

    @Override
    public int getLoadedElementsCount() {
        return loadedItemsCount;
    }

    private boolean dataLoaded = false;

    private void loadDataIfRequired() {
        while (!iterator.hasNext() && !allDataLoaded) {
            dataLoaded = false;
            getElementsOfPage(pageNumber++, new OnLoadingFinished<T>() {
                @Override
                public void onLoadingFinished(List<T> elements, boolean isLastPage) {
                    loadedItemsCount += elements.size();
                    iterator = elements.iterator();
                    allDataLoaded = isLastPage;
                    dataLoaded = true;
                }
            }, new OnError() {
                @Override
                public void onError(Throwable e) {
                    throw new RuntimeException(e);
                }
            });
            if (!dataLoaded) {
                throw new ConcurrentModificationException("Only one thread can use NavigationIterator");
            }
        }
    }

    @Override
    public boolean hasNext() {
        loadDataIfRequired();
        return iterator.hasNext();
    }

    @Override
    public T next() {
        loadDataIfRequired();
        return iterator.next();
    }
}
