package com.haiyue.messaging.controller;

import com.haiyue.messaging.annotation.NeedAuth;
import com.haiyue.messaging.enums.MessageType;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.model.Message;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.response.CommonResponse;
import com.haiyue.messaging.response.PaginatedResponse;
import com.haiyue.messaging.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/sendFile")
    @NeedAuth
    public CommonResponse sendFile(User user,
                                   @RequestParam(value = "file", required = false) MultipartFile file,
                                   @RequestParam(value = "text", required = false) String text,
                                   @RequestParam(value = "receiverUserId", required = false) Integer receiverUserId,
                                   @RequestParam(value = "groupChatId", required = false) Integer groupChatId,
                                   @RequestParam("messageType") MessageType messageType)
            throws MessageServiceException {
        this.messageService.storeMessage(user, receiverUserId, groupChatId, text, file, messageType);
        return new CommonResponse(Status.OK);
    }

    @PostMapping("/listMessages")
    @NeedAuth
    public PaginatedResponse<Message>  listMessages(
            User user,
            @RequestParam(value = "groupChatId", required = false) Integer groupChatId,
            @RequestParam(value = "receiverUserId", required = false) Integer receiverUserId,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page)
            throws MessageServiceException {
        List<Message> messages = this.messageService.listMessages(user, groupChatId, receiverUserId, page);
        return new PaginatedResponse<>(messages, page);
    }

    @PostMapping("/downloadFile")
    @NeedAuth
    public ResponseEntity<ByteArrayResource> downloadFile(User user, @RequestParam int messageId)
            throws MessageServiceException{
        byte[] bytes = this.messageService.getMessageFile(user, messageId);
        MediaType mediaType = this.messageService.getMediaType(user, messageId);
        ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);
        ResponseEntity<ByteArrayResource> responseEntity = ResponseEntity.ok()
            .contentLength(bytes.length)
            .contentType(mediaType)
            .body(byteArrayResource);
        return responseEntity;
    }
}
