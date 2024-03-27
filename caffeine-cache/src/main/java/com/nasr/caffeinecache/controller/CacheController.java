package com.nasr.caffeinecache.controller;

import com.nasr.caffeinecache.dto.UserResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nasr.caffeinecache.config.UserCacheConfig.USER_ALL;
import static com.nasr.caffeinecache.config.UserCacheConfig.USER_ID;

@RestController
@RequestMapping("/cache-info")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;


    @GetMapping("/user-id/key/{keyId}")
    public ResponseEntity<?> getUserIdCacheData(@PathVariable Long keyId) {


        return ResponseEntity.ok(
                cacheManager.getCache(USER_ID).get(keyId, UserResponseModel.class)
        );
    }

    @GetMapping("/user-all")
    public ResponseEntity<?> getUserAllCacheItems() throws IllegalAccessException {

        Cache cache = cacheManager.getCache(USER_ALL);
        if (cache instanceof CaffeineCache caffeineCache) {
            return ResponseEntity.ok(
                    caffeineCache.getNativeCache()
                            .asMap()
            );
        }
        throw new IllegalAccessException("caffeine cache provider dont supported!");
    }

}
