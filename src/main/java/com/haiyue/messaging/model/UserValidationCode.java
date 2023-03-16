package com.haiyue.messaging.model;

import lombok.Data;

@Data
public class UserValidationCode {
    private int id;
    private String username;
    private String validationCode;
}
