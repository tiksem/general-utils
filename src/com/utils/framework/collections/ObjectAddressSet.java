package com.utils.framework.collections;

import com.utils.framework.Equals;
import com.utils.framework.HashCodeProvider;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * Date: 23.03.13
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
public class ObjectAddressSet<T> extends SetWithPredicates<T> {
    public ObjectAddressSet() {
        super(new Equals() {
            @Override
            public boolean equals(Object a, Object b) {
                return a == b;
            }
        }, new HashCodeProvider() {
            @Override
            public int getHashCodeOf(Object object) {
                return System.identityHashCode(object);
            }
        });
    }
}
