package com.utils.framework.collections;

import com.utils.framework.OnError;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 06.01.13
 * Time: 18:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class NavigationList<T> extends AbstractList<T> implements NavigationEntity<T> {
    private boolean allDataLoaded = false;
    private int loadedPagesCount = 0;
    private int distanceToLoadNextPage;
    private int elementsOffset = 0;
    private int maxElementsCount;
    private List<T> elements;
    private boolean pageLoadingExecuted = false;
    private boolean pageLoadingPaused = false;
    private int lastIndexRequestedBeforePageLoading = -1;
    private OnAllDataLoaded onAllDataLoaded;
    private OnError onError;
    private boolean manualPageLoading = false;
    private OnPageLoadingRequested onPageLoadingRequested;

    public static interface OnPageLoadingFinished {
        void onLoadingFinished();
    }

    public static interface OnPageLoadingRequested {
        void onPageLoadingRequested(int index);
    }

    private OnPageLoadingFinished onPageLoadingFinished;

    public OnPageLoadingFinished getOnPageLoadingFinished() {
        return onPageLoadingFinished;
    }

    public void setOnPageLoadingFinished(OnPageLoadingFinished onPageLoadingFinished) {
        this.onPageLoadingFinished = onPageLoadingFinished;
    }

    @Override
    public int getLoadedElementsCount() {
        return elements.size() - elementsOffset;
    }

    protected boolean shouldAddElement(T element) {
        return true;
    }

    protected NavigationList(List<T> initialElements, int maxElementsCount) {
        elements = new ArrayList<T>() {
            @Override
            public boolean add(T object) {
                if (shouldAddElement(object)) {
                    return super.add(object);
                }

                return false;
            }
        };

        elements.addAll(initialElements);
        elementsOffset = initialElements.size();
        this.maxElementsCount = maxElementsCount;
    }

    protected NavigationList() {
        this(Integer.MAX_VALUE);
    }

    protected NavigationList(int maxElementsCount) {
        this(Collections.<T>emptyList(), maxElementsCount);
    }

    public int getDistanceToLoadNextPage() {
        return distanceToLoadNextPage;
    }

    public void setDistanceToLoadNextPage(int distanceToLoadNextPage) {
        this.distanceToLoadNextPage = distanceToLoadNextPage;
    }

    private boolean shouldLoadNextPage(int location) {
        return location >= getLoadedElementsCount() - distanceToLoadNextPage;
    }

    @Override
    public T get(int location) {
        if (!allDataLoaded) {
            int size = elements.size();
            if (shouldLoadNextPage(location)) {
                lastIndexRequestedBeforePageLoading = location;
                if (!manualPageLoading) {
                    loadNextPage();
                } else {
                    requestLoadNextPage(location);
                }
                if (location < size) {
                    return elements.get(location);
                }
            } else {
                return elements.get(location);
            }
        } else {
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

    public void pausePageLoading() {
        pageLoadingPaused = true;
    }

    public boolean isPageLoadingPaused() {
        return pageLoadingPaused;
    }

    public void resumePageLoading() {
        pageLoadingPaused = false;
        if (lastIndexRequestedBeforePageLoading >= 0) {
            loadNextPage();
        }
    }

    protected void onPageLoadingFinished(int pageNumber) {

    }

    private void requestLoadNextPage(int index) {
        if (onPageLoadingRequested != null) {
            onPageLoadingRequested.onPageLoadingRequested(index);
        }
    }

    public void loadNextPage() {
        loadNextPage(null);
    }

    public void loadNextPage(final OnPageLoadingFinished onPageLoadingFinished) {
        if (allDataLoaded) {
            return;
        }

        if (pageLoadingPaused) {
            return;
        }

        if (pageLoadingExecuted) {
            return;
        }

        pageLoadingExecuted = true;
        final int pageToLoad = loadedPagesCount;

        getElementsOfPage(pageToLoad, new OnLoadingFinished<T>() {
            @Override
            public void onLoadingFinished(List<T> pageElements, boolean isLastPage) {
                if (isAllDataLoaded()) {
                    return;
                }

                allDataLoaded = isLastPage;

                loadedPagesCount++;

                Iterator<T> pageElementsIterator = pageElements.iterator();
                while (pageElementsIterator.hasNext()) {
                    if (elements.size() >= maxElementsCount) {
                        allDataLoaded = true;
                        notifyAllDataLoaded();
                        break;
                    }

                    T pageElement = pageElementsIterator.next();
                    elements.add(pageElement);
                }

                onPageLoadingFinished(pageToLoad);

                if (onPageLoadingFinished != null) {
                    onPageLoadingFinished.onLoadingFinished();
                }

                OnPageLoadingFinished onPageLoadingFinished = getOnPageLoadingFinished();
                if (onPageLoadingFinished != null) {
                    onPageLoadingFinished.onLoadingFinished();
                }

                pageLoadingExecuted = false;

                if (!allDataLoaded && shouldLoadNextPage(lastIndexRequestedBeforePageLoading)) {
                    loadNextPage();
                } else {
                    lastIndexRequestedBeforePageLoading = -1;
                }

                if (allDataLoaded) {
                    notifyAllDataLoaded();
                }

            }
        }, new OnError() {
            @Override
            public void onError(Throwable e) {
                onErrorOccurred(e);
            }
        });
    }

    private void onErrorOccurred(Throwable e) {
        lastIndexRequestedBeforePageLoading = -1;
        pageLoadingExecuted = false;

        if (onError != null) {
            onError.onError(e);
        }
    }

    private void notifyAllDataLoaded() {
        onAllDataLoaded();
        if (onAllDataLoaded != null) {
            onAllDataLoaded.onAllDataLoaded();
        }
    }

    protected void onAllDataLoaded() {

    }

    @Override
    public void add(int location, T object) {
        if (location > elementsOffset) {
            throw new IndexOutOfBoundsException("Unable to insert element between loaded elements");
        }

        if (shouldAddElement(object)) {
            elements.add(location, object);
            elementsOffset++;
        }
    }

    @Override
    public int size() {
        if (allDataLoaded) {
            return elements.size();
        } else {
            return elements.size() + 1;
        }
    }

    public static NavigationList emptyList() {
        NavigationList navigationList = new NavigationList() {
            @Override
            public void getElementsOfPage(int pageNumber, OnLoadingFinished onPageLoadingFinished, OnError onError) {

            }
        };
        navigationList.allDataLoaded = true;
        return navigationList;
    }

    public static <T> NavigationList<T> decorate(List<T> elements) {
        NavigationList<T> list = new NavigationList<T>() {
            @Override
            public void getElementsOfPage(int pageNumber, OnLoadingFinished<T> onPageLoadingFinished, OnError onError) {

            }
        };

        list.allDataLoaded = true;
        list.elements = elements;

        return list;
    }

    public final int getMaxElementsCount() {
        return maxElementsCount;
    }


    public OnAllDataLoaded getOnAllDataLoaded() {
        return onAllDataLoaded;
    }

    public void setOnAllDataLoaded(OnAllDataLoaded onAllDataLoaded) {
        this.onAllDataLoaded = onAllDataLoaded;
    }

    public int getLoadedPagesCount() {
        return loadedPagesCount;
    }

    public OnError getOnError() {
        return onError;
    }

    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    public OnPageLoadingRequested getOnPageLoadingRequested() {
        return onPageLoadingRequested;
    }

    public void setOnPageLoadingRequested(OnPageLoadingRequested onPageLoadingRequested) {
        this.onPageLoadingRequested = onPageLoadingRequested;
    }

    public boolean isManualPageLoading() {
        return manualPageLoading;
    }

    public void setManualPageLoading(boolean manualPageLoading) {
        this.manualPageLoading = manualPageLoading;
    }
}
