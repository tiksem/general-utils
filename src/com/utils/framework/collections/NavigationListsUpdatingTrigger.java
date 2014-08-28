package com.utils.framework.collections;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by CM on 8/29/2014.
 */
public class NavigationListsUpdatingTrigger {
    private Set<NavigationList> navigationLists = new HashSet<NavigationList>();

    public void addNavigationList(NavigationList navigationList) {
        navigationLists.add(navigationList);
    }

    public void removeNavigationList(NavigationList navigationList) {
        navigationLists.add(navigationList);
    }

    public void addAndSetActive(NavigationList active) {
        addNavigationList(active);
        for(NavigationList navigationList : navigationLists){
            if(navigationList == active){
                navigationList.resumePageLoading();
            } else {
                navigationList.pausePageLoading();
            }
        }
    }
}
