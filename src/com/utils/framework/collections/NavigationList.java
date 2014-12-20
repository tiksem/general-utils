package com.utils.framework.collections;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 06.01.13
 * Time: 18:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class NavigationList<T> extends AbstractList<T>{
    private boolean allDataLoaded = false;
    private int loadedPagesCount = 0;
    private int distanceToLoadNextPage;
    private int elementsOffset = 0;
    private int maxElementsCount;
    private List<T> elements;
    private boolean pageLoadingExecuted = false;
    private boolean pageLoadingPaused = false;
    private int lastIndexRequestedBeforePageLoading = -1;

    public static interface OnPageLoadingFinished<T>{
        void onLoadingFinished(List<T> result, boolean isLastPage);
    }

    public static interface OnNextPageLoadingFinished{
        void onLoadingFinished();
    }

    private OnNextPageLoadingFinished onNextPageLoadingFinished;

    public OnNextPageLoadingFinished getOnNextPageLoadingFinished() {
        return onNextPageLoadingFinished;
    }

    public void setOnNextPageLoadingFinished(OnNextPageLoadingFinished onNextPageLoadingFinished) {
        if(onNextPageLoadingFinished != null && this.onNextPageLoadingFinished == null){
            if(loadedPagesCount > 0){
                onNextPageLoadingFinished.onLoadingFinished();
            }
        }

        this.onNextPageLoadingFinished = onNextPageLoadingFinished;
    }

    public int getLoadedElementsCount(){
        return elements.size() - elementsOffset;
    }

    protected NavigationList(List<T> initialElements, int maxElementsCount) {
        elements = new ArrayList<T>();
        elements.addAll(initialElements);
        elementsOffset = initialElements.size();
        this.maxElementsCount = maxElementsCount;
    }

    private NavigationList(){

    }

    protected NavigationList(int maxElementsCount) {
        this(Collections.<T>emptyList(),maxElementsCount);
    }

    public int getDistanceToLoadNextPage() {
        return distanceToLoadNextPage;
    }

    public void setDistanceToLoadNextPage(int distanceToLoadNextPage) {
        this.distanceToLoadNextPage = distanceToLoadNextPage;
    }

    private boolean shouldLoadNextPage(int location){
        return location >= getLoadedElementsCount() - distanceToLoadNextPage;
    }

    @Override
    public T get(int location) {
        if(!allDataLoaded){
            int size = elements.size();
            if(shouldLoadNextPage(location)){
                lastIndexRequestedBeforePageLoading = location;
                loadNextPage();
                if(location < size){
                    return elements.get(location);
                }
            }
            else {
                return elements.get(location);
            }
        }
        else {
            if (location < elements.size()) {
                return elements.get(location);
            } else {
                return null;
            }
        }

        return null;
    }

    public boolean isAllDataLoaded() {
        return allDataLoaded;
    }

    public void pausePageLoading(){
        pageLoadingPaused = true;
    }

    public boolean isPageLoadingPaused() {
        return pageLoadingPaused;
    }

    public void resumePageLoading(){
        pageLoadingPaused = false;
        if(lastIndexRequestedBeforePageLoading >= 0){
            loadNextPage();
        }
    }

    public void loadNextPage(){
        if(allDataLoaded){
            return;
        }

        if(pageLoadingPaused){
            return;
        }

        if(pageLoadingExecuted){
            return;
        }

        pageLoadingExecuted = true;
        final int pageToLoad = loadedPagesCount;

        getElementsOfPage(pageToLoad, new OnPageLoadingFinished<T>() {
            @Override
            public void onLoadingFinished(List<T> pageElements, boolean isLastPage) {
                if (isAllDataLoaded()) {
                    return;
                }

                allDataLoaded = isLastPage;
                if (allDataLoaded) {
                    onAllDataLoaded();
                }

                loadedPagesCount++;

                Iterator<T> pageElementsIterator = pageElements.iterator();
                while (pageElementsIterator.hasNext()) {
                    if (elements.size() >= maxElementsCount) {
                        allDataLoaded = true;
                        onAllDataLoaded();
                        break;
                    }

                    T pageElement = pageElementsIterator.next();
                    elements.add(pageElement);
                }

                OnNextPageLoadingFinished onNextPageLoadingFinished = getOnNextPageLoadingFinished();
                if (onNextPageLoadingFinished != null) {
                    onNextPageLoadingFinished.onLoadingFinished();
                }

                pageLoadingExecuted = false;

                if (!allDataLoaded && shouldLoadNextPage(lastIndexRequestedBeforePageLoading)) {
                    loadNextPage();
                } else {
                    lastIndexRequestedBeforePageLoading = -1;
                }
            }
        });
    }

    protected abstract void getElementsOfPage(int pageNumber,
                                              OnPageLoadingFinished<T> onPageLoadingFinished);

    protected void onAllDataLoaded(){

    }

    @Override
    public int size() {
        if(allDataLoaded) {
            return elements.size();
        }
        else {
            return maxElementsCount;
        }
    }

    public static NavigationList emptyList(){
        NavigationList navigationList = new NavigationList(0) {
            @Override
            protected void getElementsOfPage(int pageNumber,
                                             OnPageLoadingFinished onPageLoadingFinished) {

            }
        };
        navigationList.allDataLoaded = true;
        return navigationList;
    }

    public static <T> NavigationList<T> decorate(List<T> elements){
        NavigationList<T> list = new NavigationList<T>() {
            @Override
            protected void getElementsOfPage(int pageNumber, OnPageLoadingFinished<T> onPageLoadingFinished) {

            }
        };

        list.allDataLoaded = true;
        list.elements = elements;

        return list;
    }

    public final int getMaxElementsCount() {
        return maxElementsCount;
    }


    public static void pausePageLoadingOfNavigationList(List list){
        if(list instanceof NavigationList){
            ((NavigationList)list).pausePageLoading();
        }
    }

    public static void resumePageLoadingOfNavigationList(List list){
        if(list instanceof NavigationList){
            ((NavigationList)list).resumePageLoading();
        }
    }
}
