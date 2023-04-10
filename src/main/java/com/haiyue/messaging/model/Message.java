package com.haiyue.messaging.model;

import com.haiyue.messaging.enums.MessageType;
import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private int id;
    private int senderUserId;
    private int groupChatId;
    private int receiverUserId;
    private String content; // content is null when messageType is not TEXT
    private Date sendTime;
    private MessageType messageType;
}
