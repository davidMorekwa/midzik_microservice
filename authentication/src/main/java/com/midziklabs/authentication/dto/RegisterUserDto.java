package com.midziklabs.authentication.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String email;
    private String password;
    private String name;
    private String role_id;
}
