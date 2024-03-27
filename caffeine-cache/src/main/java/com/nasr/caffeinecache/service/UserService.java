package com.nasr.caffeinecache.service;

import com.nasr.caffeinecache.dto.CreateUserRequestModel;
import com.nasr.caffeinecache.dto.UpdateUserRequestModel;
import com.nasr.caffeinecache.dto.UserResponseModel;

public interface UserService {

    UserResponseModel getUserById(Long id);

    UserResponseModel save(CreateUserRequestModel user);

    UserResponseModel update(UpdateUserRequestModel updateUserRequest);

    void deleteUser(Long id);

    void deleteUsers();

    Iterable<UserResponseModel> saveAll(Iterable<CreateUserRequestModel> users);

    Iterable<UserResponseModel> getAll();

    long count() throws IllegalAccessException;

}
