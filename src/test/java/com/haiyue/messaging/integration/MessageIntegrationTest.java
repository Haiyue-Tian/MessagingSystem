package com.haiyue.messaging.integration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.haiyue.messaging.dao.TestFriendInvitationDAO;
import com.haiyue.messaging.dao.TestMessageDAO;
import com.haiyue.messaging.dao.TestUserDAO;
import com.haiyue.messaging.enums.FriendInvitationStatus;
import com.haiyue.messaging.enums.MessageType;
import com.haiyue.messaging.model.FriendInvitation;
import com.haiyue.messaging.model.Message;
import com.haiyue.messaging.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import static com.haiyue.messaging.utils.Constant.S3_BUCKET_NAME;
import static com.haiyue.messaging.utils.Password.passwordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class MessageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUserDAO testUserDAO;

    @Autowired
    private TestMessageDAO testMessageDAO;

    @Autowired
    private CleanUpUtils cleanUpUtils;

    @Autowired
    private TestFriendInvitationDAO testFriendInvitationDAO;

    @Autowired
    AmazonS3 amazonS3;

    @BeforeEach
    public void cleanUpData() {
        this.cleanUpUtils.cleanUpData();
    }

    @Test
    public void testSendFile_happyCaseSendText() throws Exception{
        User sender = new User();
        sender.setUsername("sender");
        sender.setEmail("123@email.com");
        sender.setPassword("password");
        sender.setIsValid(true);
        this.testUserDAO.insertUser(sender);
        sender = this.testUserDAO.selectByUsername("sender").get(0);
        String senderLoginToken = RandomStringUtils.randomAlphabetic(64);
        this.testUserDAO.login(passwordEncoder(senderLoginToken), new Date(), "sender");

        User receiver = new User();
        receiver.setUsername("receiver");
        receiver.setEmail("1234@email.com");
        receiver.setPassword("password");
        receiver.setIsValid(true);
        this.testUserDAO.insertUser(receiver);

        // set friends
        FriendInvitation friendInvitation = new FriendInvitation();
        friendInvitation.setStatus(FriendInvitationStatus.ACCEPTED);
        friendInvitation.setSenderUserId(sender.getId());
        friendInvitation.setReceiverUserId(receiver.getId());
        this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

        this.mockMvc.perform(multipart("/messages/sendFile")
                        .param("text", "text")
                        .param("receiverUserId", String.valueOf(receiver.getId()))
                        .param("messageType", "TEXT")
                        .header("Login-Token", senderLoginToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        Message message = this.testMessageDAO.selectBySenderAndReceiver(sender.getId(), receiver.getId());
        assertEquals(sender.getId(), message.getSenderUserId());
        assertEquals(receiver.getId(), message.getReceiverUserId());
        assertEquals(Date.class, message.getSendTime().getClass());
        assertEquals(MessageType.TEXT, message.getMessageType());
    }

    @Test
    public void testDownloadFile_happyCase() throws Exception{
        User sender = new User();
        sender.setUsername("sender");
        sender.setEmail("123@email.com");
        sender.setPassword("password");
        sender.setIsValid(true);
        this.testUserDAO.insertUser(sender);
        String senderLoginToken = RandomStringUtils.randomAlphabetic(64);
        this.testUserDAO.login(passwordEncoder(senderLoginToken), new Date(), "sender");

        User receiver = new User();
        receiver.setUsername("receiver");
        receiver.setEmail("1234@email.com");
        receiver.setPassword("password");
        receiver.setIsValid(true);
        this.testUserDAO.insertUser(receiver);

        // set friends
        FriendInvitation friendInvitation = new FriendInvitation();
        friendInvitation.setStatus(FriendInvitationStatus.ACCEPTED);
        friendInvitation.setSenderUserId(sender.getId());
        friendInvitation.setReceiverUserId(receiver.getId());
        this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

        // message
        Message message = new Message();
        message.setSenderUserId(sender.getId());
        message.setReceiverUserId(receiver.getId());
        message.setSendTime(new Date());
        message.setMessageType(MessageType.TEXT);
        this.testMessageDAO.insertMessage(message);

        String text = "text";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        this.amazonS3.putObject(S3_BUCKET_NAME, String.valueOf(message.getId()), inputStream, new ObjectMetadata());

        this.mockMvc.perform(multipart("/messages/downloadFile")
                        .param("messageId", String.valueOf(message.getId()))
                        .header("Login-Token", senderLoginToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().bytes(text.getBytes()))
                .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, text.length()));
    }

    @Test
    public void testListMessages_happyCase() throws Exception{

    }
}
