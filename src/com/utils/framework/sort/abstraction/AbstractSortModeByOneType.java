package com.utils.framework.sort.abstraction;

public abstract class AbstractSortModeByOneType<T> implements
        ISortModeByOneType<T> {
    protected ISortParameter<T> sortType = null;

    private SortTypeChange sortTypeListener;

    public void setSortType(ISortParameter<T> type) {
        this.sortType = type;

        if (sortTypeListener != null) {
            sortTypeListener.onSortTypeChanged();
        }
    }

    public void setSortTypeChangeListener(SortTypeChange listener) {
        this.sortTypeListener = listener;
    }

    public interface SortTypeChange {
        public void onSortTypeChanged();
    }
}
