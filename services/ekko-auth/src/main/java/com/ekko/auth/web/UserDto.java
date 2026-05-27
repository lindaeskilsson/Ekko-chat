package com.ekko.auth.web;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String username;
    private String password;
}