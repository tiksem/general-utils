package com.utils.framework.collections;

import com.utils.framework.KeyProvider;

import java.util.List;
import java.util.Set;

/**
 * Created by CM on 8/30/2015.
 */
public abstract class UniqueLazyLoadingList<T> extends LazyLoadingList<T> {
    private Set<T> set;

    protected abstract KeyProvider<Object, T> getKeyProvider();

    private void init() {
        KeyProvider<Object, T> keyProvider = getKeyProvider();

        if (keyProvider != null) {
            set = SetWithPredicates.fromKeyProvider(keyProvider);
        }
    }

    public UniqueLazyLoadingList(List<T> initialElements, int maxElementsCount) {
        super(initialElements, maxElementsCount);
        init();
    }

    public UniqueLazyLoadingList() {
        init();
    }

    public UniqueLazyLoadingList(int maxElementsCount) {
        super(maxElementsCount);
        init();
    }

    @Override
    protected boolean shouldAddElement(T element) {
        if (set == null) {
            return super.shouldAddElement(element);
        } else {
            return set.add(element);
        }
    }
}
