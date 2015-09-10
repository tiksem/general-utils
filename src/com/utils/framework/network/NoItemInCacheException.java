package com.utils.framework.network;

import java.io.IOException;

/**
 * Created by CM on 9/11/2015.
 */
public class NoItemInCacheException extends IOException {
    public NoItemInCacheException(String url) {
        super("Unable to find item in cache by url " + url);
    }
}
