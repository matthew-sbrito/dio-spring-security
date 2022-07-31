package com.techsoft.api.authentication.dto;

import lombok.Data;

@Data
public class UserDto {
    private String fullName;
    private String username;
    private String email;
    private String secretPassword;
}
