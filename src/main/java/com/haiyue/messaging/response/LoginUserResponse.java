package com.haiyue.messaging.response;

import com.haiyue.messaging.enums.Status;

public class LoginUserResponse extends CommonResponse{
    private String loginToken;
    public LoginUserResponse(String loginToken){
        super(Status.OK);
        this.loginToken = loginToken;
    }

    public String getLoginToken(){
        return loginToken;
    }
}
