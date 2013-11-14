package com.dbbest.framework.sort.abstraction;

import java.util.List;

public interface ISortMode<T, R> {
	public R[] sort(T[] data) throws Exception;
	public List<R> sort(List<T> data) throws Exception;
}
