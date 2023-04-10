package com.haiyue.messaging.model;

import lombok.Data;

import java.util.Date;

@Data
public class GroupChat {
    private int id;
    private String name;
    private String description;
    private Date createTime;
    private int creatorUserId;
}
