package com.haiyue.messaging.controller;

import com.haiyue.messaging.annotation.NeedAuth;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.request.MessageRequest;
import com.haiyue.messaging.response.CommonResponse;
import com.haiyue.messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    @NeedAuth
    public CommonResponse sendMessage(
            User user,
            @RequestBody MessageRequest messageRequest) throws MessageServiceException {
        this.messageService.sendMessage(
                user,
                messageRequest.getGroupChatId(),
                messageRequest.getReceiverUserId(),
                messageRequest.getContent(),
                messageRequest.getMessageType());
        return new CommonResponse(Status.OK);
    }

    @PostMapping("/sendFile")
    @NeedAuth
    public CommonResponse sendFile(
            User user,
            @RequestBody MessageRequest messageRequest,
            @RequestParam("file") MultipartFile file) throws MessageServiceException, IOException {
        this.messageService.sendFile(
                user,
                messageRequest.getGroupChatId(),
                messageRequest.getReceiverUserId(),
                messageRequest.getMessageType(),
                file);
        return new CommonResponse(Status.OK);
    }
}
