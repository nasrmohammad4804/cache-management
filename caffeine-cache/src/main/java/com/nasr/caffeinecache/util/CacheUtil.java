package com.nasr.caffeinecache.util;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;


public class CacheUtil {

    public static boolean isKeyExistsInCache(CacheManager cacheManager,String cacheName, Object key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            return valueWrapper != null && valueWrapper.get() != null;
        }
        return false;
    }

}
