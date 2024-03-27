package com.nasr.caffeinecache.config;

import com.github.benmanes.caffeine.cache.*;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static com.nasr.caffeinecache.config.UserCacheConfig.*;

@Configuration
public class CacheConfig {


    @Bean
    public CacheManager userCaches(){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();

        caffeineCacheManager.registerCustomCache(USER_ID, getUserByIdCache());
        caffeineCacheManager.registerCustomCache(USER_ALL, getUserAllCache());

        return  caffeineCacheManager;
    }

    private Cache<Object, Object> getUserAllCache() {
        return Caffeine.newBuilder()
                .expireAfterAccess(3, TimeUnit.MINUTES)
                .initialCapacity(100)
                .maximumSize(500)
                .recordStats()
                .build();
    }

    /*
    * we use caffeine cache provider and size & time base cache eviction policy
    *
    * */
    private Cache<Object, Object> getUserByIdCache(){
        return Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .initialCapacity(100)
                .maximumSize(300)
                .recordStats()
                .build();
    }
}
