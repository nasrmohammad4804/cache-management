package com.nasr.caffeinecache.service.impl;

import com.nasr.caffeinecache.domain.User;
import com.nasr.caffeinecache.dto.CreateUserRequestModel;
import com.nasr.caffeinecache.dto.UpdateUserRequestModel;
import com.nasr.caffeinecache.dto.UserResponseModel;
import com.nasr.caffeinecache.repository.UserRepository;
import com.nasr.caffeinecache.service.UserService;
import com.nasr.caffeinecache.util.CacheUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.nasr.caffeinecache.config.UserCacheConfig.*;
import static com.nasr.caffeinecache.config.UserCacheConfig.USER_ALL;
import static com.nasr.caffeinecache.config.UserCacheConfig.USER_ID;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CacheManager cacheManager;


    @Override
    @Cacheable(key = "#id", cacheNames = USER_ID)
    public UserResponseModel getUserById(Long id) {
        return repository.getUserById(id)
                .orElseThrow(() -> new RuntimeException("user dont find with id : " + id));

    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true, cacheNames = USER_ALL)
    public UserResponseModel save(CreateUserRequestModel user) {
        User userEntity = new User();

        BeanUtils.copyProperties(user, userEntity);
        userEntity = repository.save(userEntity);

        return new UserResponseModel(userEntity.getUsername(), userEntity.getFirstName(), userEntity.getLastName());
    }

    @Override
    @Transactional
    @CachePut(key = "#updateUserRequest.id", cacheNames = USER_ID, condition = "#result!=null")
    @CacheEvict(allEntries = true, cacheNames = USER_ALL)
    public UserResponseModel update(UpdateUserRequestModel updateUserRequest) {

        User user = repository.findById(updateUserRequest.getId())
                .orElseThrow(() -> new RuntimeException("dont find user with id : " + updateUserRequest.getId()));

        user.setFirstName(updateUserRequest.getFirstName());
        user.setLastName(updateUserRequest.getLastName());

        boolean keyExistsInCache = CacheUtil.isKeyExistsInCache(cacheManager, USER_ID, updateUserRequest.getId());
        if (keyExistsInCache) {
            return new UserResponseModel(user.getUsername(), user.getFirstName(), user.getLastName());
        } else return null;

    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = USER_ID, key = "#id"),
            @CacheEvict(cacheNames = USER_ALL, allEntries = true)
    })
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = USER_ID, allEntries = true),
            @CacheEvict(cacheNames = USER_ALL, allEntries = true)
    })
    public void deleteUsers() {
        repository.deleteAll();
    }

    @Override
    @CacheEvict(allEntries = true, cacheNames = USER_ALL)
    @Transactional
    public Iterable<UserResponseModel> saveAll(Iterable<CreateUserRequestModel> users) {

        List<User> userEntities = new ArrayList<>();
        users.forEach(user -> userEntities.add(new User(user.getUsername(), user.getFirstName(), user.getLastName(), user.getPassword())));

        return repository.saveAll(userEntities)
                .stream().map(user -> new UserResponseModel(user.getUsername(), user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = USER_ALL, keyGenerator = "emptyKeyGenerator")
    public Iterable<UserResponseModel> getAll() {
        return repository.findAll()
                .stream().map(user -> new UserResponseModel(user.getUsername(), user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public long count() throws IllegalAccessException {

        Cache cache = cacheManager.getCache(USER_ALL);

        if (cache instanceof CaffeineCache caffeineCache) {
            Object value = caffeineCache.getNativeCache()
                    .asMap()
                    .get(USER_ALL_CACHE_KEY_NAME);

            AtomicReference<Long> result = new AtomicReference<>();

            Optional.ofNullable(value)
                    .ifPresentOrElse((val) -> result.set((long) ((List<?>) val).size()),
                            () -> result.set(repository.count()));

            return result.get();
        }
        throw new IllegalAccessException("caffeine cache provider dont supported!");
    }
}
