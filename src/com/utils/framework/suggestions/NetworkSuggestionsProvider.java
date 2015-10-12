package com.utils.framework.suggestions;

import com.utils.framework.network.GetRequestExecutor;
import com.utils.framework.network.RequestExecutor;
import com.utils.framework.strings.Strings;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 7/9/2015.
 */
public abstract class NetworkSuggestionsProvider<T> implements SuggestionsProvider<T> {
    private String queryParamName = "query";
    private RequestExecutor requestExecutor;
    private Map<String, Object> args;
    private String url;

    public NetworkSuggestionsProvider(String url, Map<String, Object> args,
                                      RequestExecutor requestExecutor) {
        this.url = url;
        this.args = args != null ? new HashMap<>(args) : new HashMap<String, Object>();
        this.requestExecutor = requestExecutor;
    }

    public NetworkSuggestionsProvider(String url, Map<String, Object> args) {
        this(url, args, new GetRequestExecutor());
    }

    public String getQueryParamName() {
        return queryParamName;
    }

    public void setQueryParamName(String queryParamName) {
        this.queryParamName = queryParamName;
    }

    protected boolean shouldExecuteRequest(String query) {
        return !Strings.isEmpty(query);
    }

    @Override
    public List<T> getSuggestions(String query) {
        if (!shouldExecuteRequest(query)) {
            return null;
        }

        try {
            args.put(queryParamName, query);
            String response = requestExecutor.executeRequest(url, args);
            return parse(response);
        } catch (IOException e) {
            return null;
        }
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    protected abstract List<T> parse(String response);
}
