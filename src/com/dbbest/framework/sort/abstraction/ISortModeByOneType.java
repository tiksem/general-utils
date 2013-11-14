package com.dbbest.framework.sort.abstraction;

import java.util.List;

public interface ISortModeByOneType<T> {
	int EQUAL = 0;
	int ABOVE = 1;
	int BELOW = -1;
	
	public T[] sort(T[] data) throws Exception;
	public List<T> sort(List<T> data) throws Exception;
}
