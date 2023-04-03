package com.haiyue.messaging.request;

import lombok.Data;

@Data
public class AddFriendRequest {
    private int receiverUserId;
    private String message;
}
