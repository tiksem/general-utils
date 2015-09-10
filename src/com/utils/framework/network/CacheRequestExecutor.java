package com.utils.framework.network;

import com.utils.framework.collections.cache.Cache;
import com.utils.framework.io.Network;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by CM on 9/11/2015.
 */
public class CacheRequestExecutor implements RequestExecutor {
    private Cache<String, String> cache;

    public CacheRequestExecutor(Cache<String, String> cache) {
        this.cache = cache;
    }

    @Override
    public String executeRequest(String url, Map<String, Object> args) throws IOException {
        url = Network.getOrderedQueryStringUrl(url, args);

        String response = cache.get(url);
        if (response == null) {
            throw new NoItemInCacheException(url);
        }

        return response;
    }
}
