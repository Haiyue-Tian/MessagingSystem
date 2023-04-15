package com.haiyue.messaging.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.haiyue.messaging.dao.FriendInvitationDAO;
import com.haiyue.messaging.dao.MessageDAO;
import com.haiyue.messaging.enums.FriendInvitationStatus;
import com.haiyue.messaging.enums.MessageType;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.factory.converter.Converter;
import com.haiyue.messaging.factory.converter.ConverterFactory;
import com.haiyue.messaging.model.Message;
import com.haiyue.messaging.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static com.haiyue.messaging.utils.Constant.S3_BUCKET_NAME;
import static com.haiyue.messaging.utils.Constant.PAGE_SIZE;

@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private FriendInvitationDAO friendInvitationDAO;

    @Autowired
    AmazonS3 amazonS3;

    public void storeMessage(User user, Integer receiverUserId, Integer groupChatId, String text,
                             MultipartFile multipartFile, MessageType messageType)
            throws MessageServiceException {
        checkIsFriendOrInGroup(user.getId(), receiverUserId, groupChatId);
        var message = new Message();
        message.setSenderUserId(user.getId());
        message.setReceiverUserId(receiverUserId);
        message.setGroupChatId(groupChatId);
        message.setSendTime(new Date());
        message.setMessageType(messageType);
        this.messageDAO.insertMessage(message);

        InputStream inputStream = null;
        if (messageType != MessageType.TEXT) {
            Converter converter = ConverterFactory.getConverter(messageType);
            inputStream = converter.converter(multipartFile);
        } else {
            inputStream = new ByteArrayInputStream(text.getBytes());
        }
        this.amazonS3.putObject(S3_BUCKET_NAME, String.valueOf(message.getId()), inputStream, new ObjectMetadata());
    }

    public List<Message> listMessages(User user, Integer groupChatId, Integer receiverUserId, int page) throws MessageServiceException{
        checkIsFriendOrInGroup(user.getId(), receiverUserId, groupChatId);
        int start = (page - 1) * PAGE_SIZE;
        return this.messageDAO.selectMessages(user.getId(), groupChatId, receiverUserId, start, PAGE_SIZE + 1);
    }

    public byte[] getMessageFile(User user, int messageId) throws MessageServiceException{
        Message message = this.messageDAO.selectMessageByMessageId(messageId);
        if (message.getSenderUserId() != user.getId() && message.getReceiverUserId() != user.getId()){
            // did not integrate select user in group id
            throw new MessageServiceException(Status.NOT_AUTHORIZED);
        }
        try{
            return this.amazonS3.getObject(S3_BUCKET_NAME, String.valueOf(messageId)).getObjectContent().readAllBytes();
        } catch (IOException ioException){
            throw new MessageServiceException(Status.UNKNOWN_ERROR);
        }
    }

    public MediaType getMediaType(User user, int messageId) throws MessageServiceException{
        MessageType messageType = this.messageDAO.selectMessageTypeByMessageId(messageId);
        MediaType mediaType = null;
        if (messageType == MessageType.IMAGE) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (messageType == MessageType.TEXT) {
            mediaType = MediaType.TEXT_PLAIN;
        } else if (messageType == MessageType.VIDEO) {
            mediaType = MediaType.parseMediaType("video/mp4");
        } else if (messageType == MessageType.VOICE) {
            mediaType = MediaType.parseMediaType("audio/mpeg");
        } else if (messageType == MessageType.STICKER) {
            mediaType = MediaType.IMAGE_GIF;
        } else {
            throw new MessageServiceException(Status.CANNOT_GET_MESSAGE_TYPE);
        }
        return mediaType;
    }

    private void checkIsFriendOrInGroup(Integer senderUserId, Integer receiverUserId, Integer groupChatId)
            throws MessageServiceException{
        if (receiverUserId != null && groupChatId != null){
            throw new MessageServiceException(Status.GROUP_ID_EQUAL_TO_RECEIVER_ID);
        }
        if (receiverUserId == null && groupChatId == null){
            throw new MessageServiceException(Status.GROUP_ID_EQUAL_TO_RECEIVER_ID);
        }
        if (receiverUserId != null
                && this.friendInvitationDAO.selectStatusBySenderAndReceiver(senderUserId, receiverUserId) != FriendInvitationStatus.ACCEPTED
                && this.friendInvitationDAO.selectStatusBySenderAndReceiver(receiverUserId, senderUserId) != FriendInvitationStatus.ACCEPTED){
            throw new MessageServiceException(Status.NOT_FRIENDS);
        }
        if (groupChatId != null) {
            // check group
        }

    }
}
