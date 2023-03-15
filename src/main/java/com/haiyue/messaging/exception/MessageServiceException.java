package com.haiyue.messaging.exception;

import com.haiyue.messaging.enums.Status;

public class MessageServiceException extends Exception{

    private Status status;
    public MessageServiceException(Status status){
        super(status.getMessage());
        this.status = status;
    }
    public Status getStatus(){
        return status;
    }
}
