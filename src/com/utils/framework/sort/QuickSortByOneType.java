package com.utils.framework.sort;

import java.util.Arrays;
import java.util.List;

import com.utils.framework.sort.abstraction.AbstractSortModeByOneType;

public class QuickSortByOneType<T> extends AbstractSortModeByOneType<T> {

	@Override
	public List<T> sort(List<T> data) {
		if (sortType == null) {
			throw new IllegalArgumentException(
					"sort type is not set, please call setSortType method before");
		}

		int first = 0;
		int last = data.size() - 1;

		quickSorting(data, first, last);

		return data;
	}

	@Override
	public T[] sort(T[] data) throws Exception {
		if (sortType == null) {
			throw new IllegalArgumentException(
					"sort type is not set, please call setSortType method before");
		}

		int first = 0;
		int last = data.length - 1;
		quickSorting(Arrays.asList(data), first, last);

		return null;
	}

	private void quickSorting(List<T> array, int start, int end) {
		if (start >= end)
			return;
		int i = start, j = end;
		int mid = (i + j) / 2;
		while (i < j) {
			while ((i < mid)
					&& (sortType.typeCompare(array.get(i), array.get(mid)) <= 0))
				i++;
			while ((j > mid)
					&& (sortType.typeCompare(array.get(mid), array.get(j)) <= 0))
				j--;
			if (i < j) {
				T tempI = array.get(i);
				T tempJ = array.get(j);
				array.remove(i);
				array.add(i, tempJ);
				array.remove(j);
				array.add(j, tempI);

				if (i == mid)
					mid = j;
				else if (j == mid)
					mid = i;
			}
		}
		quickSorting(array, start, mid);
		quickSorting(array, mid + 1, end);
	}
}
