package com.utils.framework.sort.abstraction;

public abstract class AbstractSortMode<T, R> implements ISortMode<T, R> {
	protected ISortParameter<T> sortType = null;

	public void setSortType(ISortParameter<T> type) {
		this.sortType = type;
	}
}
