package com.utils.framework.network;

import com.utils.framework.CollectionUtils;
import com.utils.framework.io.IOExceptionListener;

import java.io.IOError;
import java.util.List;

/**
 * Created by CM on 2/22/2015.
 */
public abstract class NextPrevNavigation<T> {
    private List<T> navigationQueue;
    private int currentIndex;
    private Integer requestedIndex;
    private OnItemLoaded<T> onItemLoaded;

    public interface OnItemLoaded<T> {
        void onLoad(T item);
    }

    public interface OnPageLoaded<T> {
        void onPageLoaded(List<T> items);
    }

    public NextPrevNavigation(OnItemLoaded<T> onItemLoaded, T firstItem) {
        this.onItemLoaded = onItemLoaded;
        navigationQueue.add(firstItem);
        currentIndex = 0;
    }

    private void loadNextPage() {
        loadNext(navigationQueue.get(navigationQueue.size() - 1), new OnPageLoaded<T>() {
            @Override
            public void onPageLoaded(List<T> items) {
                navigationQueue.addAll(items);
                if(requestedIndex != null){
                    if(requestedIndex < navigationQueue.size() && requestedIndex >= 0){
                        T item = navigationQueue.get(requestedIndex);
                        onItemLoaded.onLoad(item);
                        requestedIndex = null;
                    } else {
                        loadNextPage();
                    }
                }
            }
        }, null);
    }

    public void next() {
        if(requestedIndex == null && currentIndex < navigationQueue.size() - 1){
            T item = navigationQueue.get(++currentIndex);
            onItemLoaded.onLoad(item);
            requestedIndex = null;
        } else {
            if (requestedIndex == null) {
                requestedIndex = currentIndex + 1;
                loadNextPage();
            } else {
                requestedIndex++;
            }
        }
    }

    private void loadPrevPage() {
        loadPrev(navigationQueue.get(0), new OnPageLoaded<T>() {
            @Override
            public void onPageLoaded(List<T> items) {
                CollectionUtils.addAllInReverseOrder(navigationQueue, 0, items);
                if(requestedIndex != null){
                    if(requestedIndex < navigationQueue.size() && requestedIndex >= 0){
                        T item = navigationQueue.get(requestedIndex);
                        onItemLoaded.onLoad(item);
                        requestedIndex = null;
                    } else {
                        loadPrevPage();
                    }
                }
            }
        }, null);
    }

    public void prev() {
        if(requestedIndex == null && currentIndex > 0){
            T item = navigationQueue.get(--currentIndex);
            onItemLoaded.onLoad(item);
            requestedIndex = null;
        } else {
            if (requestedIndex == null) {
                requestedIndex = currentIndex - 1;
                loadPrevPage();
            } else {
                requestedIndex--;
            }
        }
    }

    protected abstract void loadNext(T item, OnPageLoaded<T> onPageLoaded,
                                     IOExceptionListener ioExceptionListener);
    protected abstract void loadPrev(T item, OnPageLoaded<T> onPageLoaded,
                                     IOExceptionListener ioExceptionListener);
}
