package com.haiyue.messaging.enums;

import org.springframework.http.HttpStatus;

public enum Status {
    OK(1000, "Success", HttpStatus.OK),
    PASSWORD_NOT_MATCHED(1001, "Passwords are not matched", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(1002, "Username already exists", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1003, "Email already exists", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT(1004, "Password is too short", HttpStatus.BAD_REQUEST),
    FAILED_TO_SEND_EMAIL(1005, "Failed to send email", HttpStatus.BAD_REQUEST),
    VALIDATION_CODE_NOT_MATCHED(1006, "Validation code not matched", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_FOUND(1007, "Username not found", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(1008, "User does not exist", HttpStatus.BAD_REQUEST),
    USER_NOT_VALIDATED(1009, "User is not validated", HttpStatus.BAD_REQUEST),
    FORBIDDEN(1010, "Forbidden", HttpStatus.FORBIDDEN),
    INVITATION_NOT_EXIST_OR_EXPIRED(1011, "Invitation not found or invitation has been accepted or rejected", HttpStatus.BAD_REQUEST),
    CANNOT_SELF_ACCEPT(1012, "Cannot self-accept", HttpStatus.BAD_REQUEST),
    CANNOT_SELF_REJECT(1013, "Cannot self-reject", HttpStatus.BAD_REQUEST),
    MESSAGE_TYPE_NOT_TEXT(1014, "Message type is not TEXT", HttpStatus.BAD_REQUEST),
    MESSAGE_TYPE_IS_TEXT(1015, "Message type is TEXT", HttpStatus.BAD_REQUEST),
    GROUP_ID_EQUAL_TO_RECEIVER_ID(1016, "Message cannot be sent to both a group and a receiver at the same time or no group and receiver indicated", HttpStatus.BAD_REQUEST),
    CONTENT_IS_NULL(1017, "Content cannot be null", HttpStatus.BAD_REQUEST),
    NOT_FRIENDS(1018, "Sender and receiver are not friends", HttpStatus.BAD_REQUEST),
    CANNOT_GET_MESSAGE_TYPE(1019, "Cannot get message type", HttpStatus.BAD_REQUEST),
    NOT_AUTHORIZED(1020, "Not authorized", HttpStatus.BAD_REQUEST),
    UNKNOWN_MESSAGE_TYPE(1021, "Unknown message typoe", HttpStatus.BAD_REQUEST),
    UNKNOWN_ERROR(9999,  "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);

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
