package com.nasr.caffeinecache.repository;

import com.nasr.caffeinecache.domain.User;
import com.nasr.caffeinecache.dto.UserResponseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("""
            select new com.nasr.caffeinecache.dto.UserResponseModel(u.username,u.firstName,u.lastName)  from User  u where u.id=:id
            """)
    Optional<UserResponseModel> getUserById(Long id);
}
