package com.haiyue.messaging.request;

import lombok.Data;

@Data
public class ValidationCodeRequest {
    private int id;
    private String validationCode;
}
