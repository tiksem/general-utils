package com.utils.framework.collections.checkers;

import com.utils.framework.CollectionUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 03.03.13
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public abstract class ElementCheckerListPriorityProvider<T> implements CollectionUtils.PrioritiesProvider<T> {
    private List<ElementChecker<T>> elementCheckers;

    protected abstract List<ElementChecker<T>> getElementCheckers();

    @Override
    public int getPriorityOf(Object object, int index) {
        if (elementCheckers == null) {
            elementCheckers = getElementCheckers();
        }

        for (int i = 0; i < elementCheckers.size(); i++) {
            ElementChecker elementChecker = elementCheckers.get(i);
            if (elementChecker.elementSatisfyCondition(object, index)) {
                return i;
            }
        }

        // element does not satisfy and condition
        return elementCheckers.size();
    }

    @Override
    public int getPrioritiesCount() {
        if (elementCheckers == null) {
            elementCheckers = getElementCheckers();
        }

        return elementCheckers.size() + 1;
    }
}
