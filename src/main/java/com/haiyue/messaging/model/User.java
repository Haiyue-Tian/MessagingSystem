package com.haiyue.messaging.model;

import com.haiyue.messaging.enums.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int id;
    private String username;
    private String nickname;
    private String password;
    private String address;
    private String loginToken;
    private Date registerTime;
    private Date lastLoginTime;
    private Gender gender;
    private String email;
    private Boolean isValid;
}
