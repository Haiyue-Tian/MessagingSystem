package com.haiyue.messaging.model;

import com.haiyue.messaging.enums.MessageType;
import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private int id;
    private Integer senderUserId;
    private Integer groupChatId;
    private Integer receiverUserId;
    private String fileName;
    private Date sendTime;
    private MessageType messageType;
}
