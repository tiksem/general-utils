package com.utils.framework.collections;

import com.utils.framework.OnError;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class InfiniteLoadingNavigationList<T> extends NavigationList<T> {
    private static InfiniteLoadingNavigationList instance;

    public static <T> InfiniteLoadingNavigationList<T> get() {
        if (instance == null) {
            instance = new InfiniteLoadingNavigationList();
        }

        return instance;
    }

    @Override
    public void getElementsOfPage(int pageNumber, OnLoadingFinished<T> onPageLoadingFinished, OnError onError) {
        // loading never finixhed
    }
}
