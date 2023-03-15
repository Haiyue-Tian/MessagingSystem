package com.haiyue.messaging.enums;

import org.springframework.http.HttpStatus;

public enum Status {
    OK(1000, "Success", HttpStatus.OK),
    PASSWORD_NOT_MATCHED(1001, "Passwords are not matched", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(1002, "Username already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1003, "Email already exists", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT(1004, "Password is too short", HttpStatus.BAD_REQUEST),
    FAILED_TO_SEND_EMAIL(1005, "Failed to send email", HttpStatus.BAD_REQUEST),
    VALIDATION_CODE_NOT_MATCHED(1006, "Validation code not mathced", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatus httpStatus;

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    Status(int code, String message, HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
