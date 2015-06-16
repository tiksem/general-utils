package com.utils.framework.io;

import com.utils.framework.collections.cache.Cache;

import java.io.IOException;
import java.util.Map;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 2/25/13
 * Time: 2:27 PM
 */
public final class TextLoader {
    private Cache<String, String> memoryCache;
    private Cache<String, String> diskCache;

    public TextLoader(TextLoaderConfig config) {
        config.initUninitializedFields();
        memoryCache = config.memoryCache;
        diskCache = config.diskCache;
    }

    public String getTextFromUrl(String url) throws IOException {
        String result = memoryCache.get(url);
        if (result == null) {
            result = diskCache.get(url);
            if (result == null) {
                result = Network.executeRequestGET(url);
                diskCache.put(url, result);
            }
            memoryCache.put(url, result);
        }

        return result;
    }

    public String getTextFromUrl(String url, Map<String, Object> params) throws IOException {
        url = Network.getUrl(url, params);
        return getTextFromUrl(url);
    }
}
