package com.nasr.caffeinecache.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.nasr.caffeinecache.config.UserCacheConfig.*;

@Component
public class EmptyKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        return USER_ALL_CACHE_KEY_NAME;
    }
}
