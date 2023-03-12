package com.haiyue.messaging.request;

import com.haiyue.messaging.enums.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterUserRequest {
    private String username;
    private String nickname;
    private String password;
    private String repeatPassword;
    private String address;
    private Gender gender;
    private String email;
}
