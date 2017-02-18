package com.utils.framework.collections;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by CM on 8/29/2014.
 */
public class NavigationListsUpdatingTrigger {
    private Set<LazyLoadingList> lazyLoadingLists = new HashSet<LazyLoadingList>();

    public void addNavigationList(LazyLoadingList lazyLoadingList) {
        lazyLoadingLists.add(lazyLoadingList);
    }

    public void removeNavigationList(LazyLoadingList lazyLoadingList) {
        lazyLoadingLists.add(lazyLoadingList);
    }

    public void addAndSetActive(LazyLoadingList active) {
        addNavigationList(active);
        for (LazyLoadingList lazyLoadingList : lazyLoadingLists) {
            if (lazyLoadingList == active) {
                lazyLoadingList.resumePageLoading();
            } else {
                lazyLoadingList.pausePageLoading();
            }
        }
    }
}
