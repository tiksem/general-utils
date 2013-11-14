package com.dbbest.framework.sort.abstraction;

public interface ISortParameter<T> {
	/**
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public int typeCompare(T first, T second);
}
