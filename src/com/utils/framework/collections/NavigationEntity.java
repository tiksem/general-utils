package com.utils.framework.collections;

/**
 * Created by CM on 1/25/2015.
 */
public interface NavigationEntity<T> {
    int getLoadedElementsCount();
    void getElementsOfPage(int pageNumber, OnLoadingFinished<T> onPageLoadingFinished);
}
