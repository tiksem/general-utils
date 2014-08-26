package com.utils.framework.collections.checkers;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 03.03.13
 * Time: 17:20
 * To change this template use File | Settings | File Templates.
 */
public class ElementCheckersAnd implements ElementChecker{
    private ElementChecker[] elementCheckers;

    public ElementCheckersAnd(ElementChecker... elementCheckers) {
        this.elementCheckers = elementCheckers;
    }

    @Override
    public boolean elementSatisfyCondition(Object object, int index) {
        for(ElementChecker elementChecker : elementCheckers){
            if(!elementChecker.elementSatisfyCondition(object, index)){
                return false;
            }
        }

        return true;
    }
}
