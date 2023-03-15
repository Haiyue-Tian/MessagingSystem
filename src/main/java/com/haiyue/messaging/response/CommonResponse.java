package com.haiyue.messaging.response;

import com.haiyue.messaging.enums.Status;
import lombok.Data;

@Data
public class CommonResponse {
    private int code;
    private String message;

    public CommonResponse(Status status){
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}
