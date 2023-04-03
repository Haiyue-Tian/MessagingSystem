package com.haiyue.messaging.response;

import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.model.User;

import java.util.List;

public class ListUsersResponse extends CommonResponse {
    private List<User> users;

    public ListUsersResponse(List<User> users){
        super(Status.OK);
        this.users = users;
    }

    public List<User> getUsers(){
        return this.users;
    }
}
