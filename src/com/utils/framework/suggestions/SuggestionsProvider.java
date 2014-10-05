package com.utils.framework.suggestions;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 03.02.13
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public interface SuggestionsProvider<T> {
    List<T> getSuggestions(String query);
}
