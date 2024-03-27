package com.nasr.caffeinecache.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data

public class CreateUserRequestModel {

    private String username;

    private String firstName;

    private String lastName;

    private String password;
}
