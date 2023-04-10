package com.haiyue.messaging.request;

import com.haiyue.messaging.enums.MessageType;
import lombok.Data;

@Data
public class MessageRequest {
    private int groupChatId;
    private int receiverUserId;
    private String content;
    private MessageType messageType;
}
