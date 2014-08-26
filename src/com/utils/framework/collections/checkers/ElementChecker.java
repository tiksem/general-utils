package com.utils.framework.collections.checkers;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 03.03.13
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public interface ElementChecker<T> {
    boolean elementSatisfyCondition(T object, int index);
}
