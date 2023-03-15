package com.haiyue.messaging.model;

import lombok.Data;

@Data
public class UserValidationCode {
    private int id;
    private int userId;
    private String validationCode;
}
