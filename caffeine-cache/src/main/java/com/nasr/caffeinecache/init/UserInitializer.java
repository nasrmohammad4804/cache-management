package com.nasr.caffeinecache.init;

import com.nasr.caffeinecache.domain.User;
import com.nasr.caffeinecache.dto.CreateUserRequestModel;
import com.nasr.caffeinecache.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserInitializer {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initUsers(){
        if (userService.count()==0)
            initialize();
    }
    private void initialize(){

        List<CreateUserRequestModel> users=  List.of(
                 CreateUserRequestModel.builder().firstName("mohammad").lastName("nasr").username("mmn4804").password("1234").build(),
                 CreateUserRequestModel.builder().firstName("javad").lastName("ahmadi").username("javad451").password("122143A").build(),
                 CreateUserRequestModel.builder().firstName("hosein").lastName("zare").username("HOS123").password("cSFQ").build()
        );

        userService.saveAll(users);
    }
}
