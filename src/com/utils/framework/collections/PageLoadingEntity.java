package com.utils.framework.collections;

import com.utils.framework.OnError;

/**
 * Created by CM on 1/25/2015.
 */
public interface PageLoadingEntity<T> {
    int getLoadedElementsCount();

    void getElementsOfPage(int pageNumber, OnLoadingFinished<T> onPageLoadingFinished, OnError onError);
}
