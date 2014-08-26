package com.utils.framework.io;

import com.utils.framework.collections.cache.*;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 2/25/13
 * Time: 2:49 PM
 */
public class TextLoaderConfig {
    Cache<String,String> memoryCache;
    Cache<String,String> diskCache;
    boolean allowMemoryCaching = true;
    boolean allowDiskCaching = true;

    public void setMemoryCache(Cache<String,String> memoryCache){
        TextLoaderConfig.this.memoryCache = memoryCache;
    }

    public void setMemoryCacheSize(int sizeInBytes){
        this.memoryCache = new StringLruCache(sizeInBytes);
    }

    public void setDiskCache(Cache<String,String> diskCache){
        this.diskCache = diskCache;
    }

    public void setAllowMemoryCaching(boolean allowMemoryCaching) {
        this.allowMemoryCaching = allowMemoryCaching;
    }

    public void setAllowDiskCaching(boolean allowDiskCaching) {
        this.allowDiskCaching = allowDiskCaching;
    }

    public void setDiskCacheSize(int sizeInBytes, CacheDirectoryPathGenerator cacheDirectory){
        diskCache = new StringLruDiskCache(sizeInBytes, cacheDirectory);
    }

    public void setDiskCachePath(CacheDirectoryPathGenerator cacheDirectory){
        setDiskCacheSize(1024 * 1024 * 5, cacheDirectory);
    }

    void initUninitializedFields(){
        if(allowMemoryCaching && memoryCache == null){
            setMemoryCacheSize(1024 * 1024);
        } else if(!allowMemoryCaching) {
            memoryCache = new EmptyCache<String, String>();
        }

        if(allowDiskCaching && diskCache == null){
            throw new NullPointerException("call setDiskCache or setDiskCacheSize or setDiskCachePath");
        } else if(!allowDiskCaching) {
            diskCache = new EmptyCache<String, String>();
        }
    }
}
