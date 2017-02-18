package com.utils.framework.collections;

import com.utils.framework.OnError;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class InfiniteLoadingLazyList<T> extends LazyLoadingList<T> {
    private static InfiniteLoadingLazyList instance;

    public static <T> InfiniteLoadingLazyList<T> get() {
        if (instance == null) {
            instance = new InfiniteLoadingLazyList();
        }

        return instance;
    }

    @Override
    public void getElementsOfPage(int pageNumber, OnLoadingFinished<T> onPageLoadingFinished, OnError onError) {
        // loading never finixhed
    }
}
