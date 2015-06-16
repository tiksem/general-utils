package com.utils.framework.collections.checkers;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 03.03.13
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
public class ElementCheckersOr implements ElementChecker {
    private ElementChecker[] elementCheckers;

    public ElementCheckersOr(ElementChecker... elementCheckers) {
        this.elementCheckers = elementCheckers;
    }

    @Override
    public boolean elementSatisfyCondition(Object object, int index) {
        for (ElementChecker elementChecker : elementCheckers) {
            if (elementChecker.elementSatisfyCondition(object, index)) {
                return true;
            }
        }

        return false;
    }
}
