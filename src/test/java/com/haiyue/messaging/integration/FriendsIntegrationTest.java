package com.haiyue.messaging.integration;

import com.haiyue.messaging.dao.TestFriendInvitationDAO;
import com.haiyue.messaging.dao.TestUserDAO;
import com.haiyue.messaging.enums.FriendInvitationStatus;
import com.haiyue.messaging.model.FriendInvitation;
import com.haiyue.messaging.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static com.haiyue.messaging.utils.Password.passwordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class FriendsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUserDAO testUserDAO;

    @Autowired
    private TestFriendInvitationDAO testFriendInvitationDAO;

    @Autowired
    private CleanUpUtils cleanUpUtils;

    @BeforeEach
    public void cleanUpData(){
        this.cleanUpUtils.cleanUpData();
    }

    @Test
    public void testAddFriends_happyCase() throws Exception{
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

        String requestBody = "{\n" +
                "    \"receiverUserId\": " + Integer.toString(receiver.getId()) + ",\n" +
                "    \"message\": \"add friends\"\n" +
                "}";

        this.mockMvc.perform(post("/friends/add")
                    .header("Login-Token", senderLoginToken)
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        FriendInvitation friendInvitation = this.testFriendInvitationDAO.selectBySenderUserId(sender.getId());
        assertEquals(sender.getId(), friendInvitation.getSenderUserId());
        assertEquals(receiver.getId(), friendInvitation.getReceiverUserId());
        assertEquals("add friends", friendInvitation.getMessage());
        assertEquals(FriendInvitationStatus.PENDING, friendInvitation.getStatus());
        assertEquals(Date.class, friendInvitation.getCreateTime().getClass());
    }
}
