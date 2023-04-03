package com.haiyue.messaging.model;

import com.haiyue.messaging.enums.FriendInvitationStatus;
import lombok.Data;

import java.util.Date;

@Data
public class FriendInvitation {
    private int id;
    private int senderUserId;
    private int receiverUserId;
    private String message;
    private FriendInvitationStatus status;
    private Date createTime;
    private Date acceptTime;
}
