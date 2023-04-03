package com.haiyue.messaging.request;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String identification; // email or username
    private String password;
}
