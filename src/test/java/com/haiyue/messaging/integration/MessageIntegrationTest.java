package com.haiyue.messaging.integration;

import com.haiyue.messaging.dao.TestFriendInvitationDAO;
import com.haiyue.messaging.dao.TestMessageDAO;
import com.haiyue.messaging.dao.TestUserDAO;
import com.haiyue.messaging.enums.FriendInvitationStatus;
import com.haiyue.messaging.enums.MessageType;
import com.haiyue.messaging.model.FriendInvitation;
import com.haiyue.messaging.model.Message;
import com.haiyue.messaging.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.Date;

import static com.haiyue.messaging.utils.Password.passwordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    public void cleanUpData() {
        this.cleanUpUtils.cleanUpData();
    }


    @Test
    public void testSendMessage_happyCase() throws Exception{
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
        receiver = this.testUserDAO.selectByUsername("receiver").get(0);

        // set friends
        FriendInvitation friendInvitation = new FriendInvitation();
        friendInvitation.setStatus(FriendInvitationStatus.ACCEPTED);
        friendInvitation.setSenderUserId(sender.getId());
        friendInvitation.setReceiverUserId(receiver.getId());
        this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

        String requestBody = "{\n" +
                "    \"receiverUserId\": " + receiver.getId() + ",\n" +
                "    \"content\": \"this is a message\",\n" +
                "    \"messageType\": \"TEXT\"\n" +
                "}";

        this.mockMvc.perform(post("/messages/send")
                        .header("Login-Token", senderLoginToken)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        Message message = this.testMessageDAO.selectBySenderAndReceiver(sender.getId(), receiver.getId());
        assertEquals(sender.getId(), message.getSenderUserId());
        assertEquals(receiver.getId(), message.getReceiverUserId());
        assertEquals("this is a message", message.getContent());
        assertEquals(Date.class, message.getSendTime().getClass());
        assertEquals(MessageType.TEXT, message.getMessageType());
    }

    @Test
    public void testSendFile_happyCase() throws Exception{
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
        receiver = this.testUserDAO.selectByUsername("receiver").get(0);

        // set friends
        FriendInvitation friendInvitation = new FriendInvitation();
        friendInvitation.setStatus(FriendInvitationStatus.ACCEPTED);
        friendInvitation.setSenderUserId(sender.getId());
        friendInvitation.setReceiverUserId(receiver.getId());
        this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

        String requestBody = "{\n" +
                "    \"receiverUserId\": " + receiver.getId() + ",\n" +
                "    \"content\": \"this is a message\",\n" +
                "    \"messageType\": \"VIDEO\"\n" +
                "}";

        byte[] videoBytes = new byte[]{0x00, 0x01, 0x02, 0x03};
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", // the parameter name specified in the controller method
                "video.mp4", // the original file name
                MediaType.APPLICATION_OCTET_STREAM_VALUE, // the content type of the file
                videoBytes);

        this.mockMvc.perform(multipart("/messages/sendFile")
                        .file(mockFile)
                        .header("Login-Token", senderLoginToken)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        File file = new File("./files/video.mp4");
        file.delete();

        Message message = this.testMessageDAO.selectBySenderAndReceiver(sender.getId(), receiver.getId());
        assertEquals(sender.getId(), message.getSenderUserId());
        assertEquals(receiver.getId(), message.getReceiverUserId());
        assertEquals("./files/video.mp4", message.getContent());
        assertEquals(Date.class, message.getSendTime().getClass());
        assertEquals(MessageType.VIDEO, message.getMessageType());
    }
}
