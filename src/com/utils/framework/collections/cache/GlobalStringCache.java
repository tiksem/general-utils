package com.utils.framework.collections.cache;

/**
 * Created with IntelliJ IDEA.
 *
 * Date: 23.03.13
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */
public class GlobalStringCache implements SetCache<String>{
    private static GlobalStringCache instance;
    private SetCache<String> stringCache;

    private GlobalStringCache() {
        this.stringCache = CacheUtils.setCacheFromCache(new WeakCache<String, String>());
    }

    public static GlobalStringCache getInstance(){
        if(instance == null){
            instance = new GlobalStringCache();
        }

        return instance;
    }

    @Override
    public String get(String key) {
        return stringCache.get(key);
    }

    @Override
    public String putOrGet(String key) {
        return stringCache.putOrGet(key);
    }

    @Override
    public boolean contains(String key) {
        return stringCache.contains(key);
    }
}
