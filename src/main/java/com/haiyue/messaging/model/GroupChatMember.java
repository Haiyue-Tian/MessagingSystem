package com.haiyue.messaging.model;

import lombok.Data;

import java.util.Date;

@Data
public class GroupChatMember {
    private int id;
    private int userId;
    private int groupChatId;
    private Date joinTime;
    private boolean isAdmin;
}
