package com.utils.framework.collections;

import java.util.List;

/**
* Created by CM on 1/25/2015.
*/
public interface OnLoadingFinished<T> {
    void onLoadingFinished(List<T> elements, boolean isLastPage);
}
