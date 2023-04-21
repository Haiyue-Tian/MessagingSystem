package com.haiyue.messaging.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.haiyue.messaging.enums.Gender;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("password")
    private String password;
    @JsonProperty("repeatPassword")
    private String repeatPassword;
    @JsonProperty("address")
    private String address;
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("email")
    private String email;
}
