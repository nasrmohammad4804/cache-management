package com.nasr.caffeinecache.controller;

import com.nasr.caffeinecache.dto.CreateUserRequestModel;
import com.nasr.caffeinecache.dto.UpdateUserRequestModel;
import com.nasr.caffeinecache.dto.UserResponseModel;
import com.nasr.caffeinecache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserResponseModel user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody CreateUserRequestModel model) {
        UserResponseModel responseModel = userService.save(model);

        return ResponseEntity.status(CREATED)
                .body(responseModel);

    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequestModel model) {
        UserResponseModel updateModel = userService.update(model);
        return ResponseEntity.ok(updateModel);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAll()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.status(NO_CONTENT)
                .build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        userService.deleteUsers();

        return ResponseEntity.status(NO_CONTENT)
                .build();
    }
}
