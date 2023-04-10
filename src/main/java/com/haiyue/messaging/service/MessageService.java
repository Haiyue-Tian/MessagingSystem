package com.haiyue.messaging.service;

import com.haiyue.messaging.dao.FriendInvitationDAO;
import com.haiyue.messaging.dao.MessageDAO;
import com.haiyue.messaging.enums.FriendInvitationStatus;
import com.haiyue.messaging.enums.MessageType;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.model.Message;
import com.haiyue.messaging.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private FriendInvitationDAO friendInvitationDAO;

    public void sendMessage(
            User user,
            int groupChatId,
            int receiverUserId,
            String content,
            MessageType messageType) throws MessageServiceException{
        if (receiverUserId != 0
                && this.friendInvitationDAO.selectStatusBySenderAndReceiver(user.getId(), receiverUserId) != FriendInvitationStatus.ACCEPTED
                && this.friendInvitationDAO.selectStatusBySenderAndReceiver(receiverUserId, user.getId()) != FriendInvitationStatus.ACCEPTED){
            throw new MessageServiceException(Status.NOT_FRIENDS);
        }
        if (messageType != MessageType.TEXT) {
            throw new MessageServiceException(Status.MESSAGE_TYPE_NOT_TEXT);
        }
        if (content == null){
            throw new MessageServiceException(Status.CONTENT_IS_NULL);
        }
        if (Objects.equals(groupChatId, receiverUserId)) {
            throw new MessageServiceException(Status.GROUP_ID_EQUAL_TO_RECEIVER_ID);
        }
        Message message = new Message();
        message.setSenderUserId(user.getId());
        message.setGroupChatId(groupChatId);
        message.setReceiverUserId(receiverUserId);
        message.setContent(content);
        message.setSendTime(new Date());
        message.setMessageType(messageType);
        this.messageDAO.insertMessage(message);

        // Notification
    }

    public void sendFile(
            User user,
            int groupChatId,
            int receiverUserId,
            MessageType messageType,
            MultipartFile file) throws MessageServiceException, IOException {
        if (receiverUserId != 0
                && this.friendInvitationDAO.selectStatusBySenderAndReceiver(user.getId(), receiverUserId) != FriendInvitationStatus.ACCEPTED
                && this.friendInvitationDAO.selectStatusBySenderAndReceiver(receiverUserId, user.getId()) != FriendInvitationStatus.ACCEPTED){
            throw new MessageServiceException(Status.NOT_FRIENDS);
        }
        if (messageType == MessageType.TEXT) {
            throw new MessageServiceException(Status.MESSAGE_TYPE_IS_TEXT);
        }
        if (Objects.equals(groupChatId, receiverUserId)) {
            throw new MessageServiceException(Status.GROUP_ID_EQUAL_TO_RECEIVER_ID);
        }
        File tempFile = new File("./files", file.getOriginalFilename());
        file.transferTo(tempFile);
        Message message = new Message();
        message.setSenderUserId(user.getId());
        message.setGroupChatId(groupChatId);
        message.setReceiverUserId(receiverUserId);
        message.setContent(tempFile.getPath());
        message.setSendTime(new Date());
        message.setMessageType(messageType);
        this.messageDAO.insertMessage(message);

        // Notification
    }
}
