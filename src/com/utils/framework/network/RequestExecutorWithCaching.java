package com.utils.framework.network;

import com.utils.framework.collections.cache.Cache;
import com.utils.framework.io.Network;

import java.io.IOException;
import java.util.Map;

/**
 * Created by CM on 9/11/2015.
 */
public class RequestExecutorWithCaching implements RequestExecutor {
    private RequestExecutor networkRequestExecutor;
    private Cache<String, String> cache;
    private CacheRequestExecutor cacheRequestExecutor;

    public RequestExecutorWithCaching(RequestExecutor networkRequestExecutor, Cache<String, String> cache) {
        this.networkRequestExecutor = networkRequestExecutor;
        this.cache = cache;
        cacheRequestExecutor = new CacheRequestExecutor(cache);
    }

    public String executeRequest(String url, Map<String, Object> args, boolean useCaching) throws IOException {
        if (useCaching) {
            url = Network.getOrderedQueryStringUrl(url, args);
            try {
                String response = networkRequestExecutor.executeRequest(url, null);
                cache.put(url, response);
                return response;
            } catch (IOException e) {
                return cacheRequestExecutor.executeRequest(url, null);
            }
        } else {
            return networkRequestExecutor.executeRequest(url, args);
        }
    }

    @Override
    public String executeRequest(String url, Map<String, Object> args) throws IOException {
        return executeRequest(url, args, true);
    }
}
