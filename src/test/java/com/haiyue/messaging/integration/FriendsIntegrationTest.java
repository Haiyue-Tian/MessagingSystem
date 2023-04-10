package com.haiyue.messaging.integration;

import com.haiyue.messaging.dao.TestFriendInvitationDAO;
import com.haiyue.messaging.dao.TestUserDAO;
import com.haiyue.messaging.enums.FriendInvitationStatus;
import com.haiyue.messaging.model.FriendInvitation;
import com.haiyue.messaging.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
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
    public void cleanUpData(TestInfo testInfo){
        this.cleanUpUtils.cleanUpData();
        if (testInfo.getTestMethod().get().getName().equals("testListPendingFriendInvitation_happyCase") ||
                testInfo.getTestMethod().get().getName().equals("testListFriend_happyCase")){
            FriendInvitation friendInvitation = new FriendInvitation();
            FriendInvitation friendInvitationReverse = new FriendInvitation();

            // set user a
            User a = new User();
            a.setUsername("a");
            a.setIsValid(true);
            this.testUserDAO.insertUser(a);
            a = this.testUserDAO.selectByUsername("a").get(0);
            friendInvitation.setSenderUserId(a.getId());
            friendInvitationReverse.setReceiverUserId(a.getId());

            // set a -> b: PENDING
            User b = new User();
            b.setUsername("b");
            b.setIsValid(true);
            this.testUserDAO.insertUser(b);
            b = this.testUserDAO.selectByUsername("b").get(0);

            friendInvitation.setReceiverUserId(b.getId());
            friendInvitation.setStatus(FriendInvitationStatus.PENDING);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set a -> c: PENDING
            User c = new User();
            c.setUsername("c");
            c.setIsValid(true);
            this.testUserDAO.insertUser(c);
            c = this.testUserDAO.selectByUsername("c").get(0);

            friendInvitation.setReceiverUserId(c.getId());
            friendInvitation.setStatus(FriendInvitationStatus.PENDING);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set c -> a: PENDING
            friendInvitationReverse.setSenderUserId(c.getId());
            friendInvitationReverse.setStatus(FriendInvitationStatus.PENDING);
            friendInvitationReverse.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitationReverse);

            // set a -> d: ACCEPTED
            User d = new User();
            d.setUsername("d");
            d.setIsValid(true);
            this.testUserDAO.insertUser(d);
            d = this.testUserDAO.selectByUsername("d").get(0);

            friendInvitation.setReceiverUserId(d.getId());
            friendInvitation.setStatus(FriendInvitationStatus.ACCEPTED);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set d -> a: PENDING
            friendInvitationReverse.setSenderUserId(d.getId());
            friendInvitationReverse.setStatus(FriendInvitationStatus.PENDING);
            friendInvitationReverse.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitationReverse);

            // set a -> e: REJECTED
            User e = new User();
            e.setUsername("e");
            e.setIsValid(true);
            this.testUserDAO.insertUser(e);
            e = this.testUserDAO.selectByUsername("e").get(0);

            friendInvitation.setReceiverUserId(e.getId());
            friendInvitation.setStatus(FriendInvitationStatus.REJECTED);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set e -> a: PENDING
            friendInvitationReverse.setSenderUserId(e.getId());
            friendInvitationReverse.setStatus(FriendInvitationStatus.PENDING);
            friendInvitationReverse.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitationReverse);

            // set a -> f: PENDING
            User f = new User();
            f.setUsername("f");
            f.setIsValid(true);
            this.testUserDAO.insertUser(f);
            f = this.testUserDAO.selectByUsername("f").get(0);

            friendInvitation.setReceiverUserId(f.getId());
            friendInvitation.setStatus(FriendInvitationStatus.PENDING);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set f -> a: ACCEPTED
            friendInvitationReverse.setSenderUserId(f.getId());
            friendInvitationReverse.setStatus(FriendInvitationStatus.ACCEPTED);
            friendInvitationReverse.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitationReverse);

            // set a -> g: ACCEPTED
            User g = new User();
            g.setUsername("g");
            g.setIsValid(true);
            this.testUserDAO.insertUser(g);
            g = this.testUserDAO.selectByUsername("g").get(0);

            friendInvitation.setReceiverUserId(g.getId());
            friendInvitation.setStatus(FriendInvitationStatus.ACCEPTED);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set a -> h: REJECTED
            User h = new User();
            h.setUsername("h");
            h.setIsValid(true);
            this.testUserDAO.insertUser(h);
            h = this.testUserDAO.selectByUsername("h").get(0);

            friendInvitation.setReceiverUserId(h.getId());
            friendInvitation.setStatus(FriendInvitationStatus.REJECTED);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set h -> a: ACCEPTED
            friendInvitationReverse.setSenderUserId(h.getId());
            friendInvitationReverse.setStatus(FriendInvitationStatus.ACCEPTED);
            friendInvitationReverse.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitationReverse);

            // set a -> i: PENDING
            User i = new User();
            i.setUsername("i");
            i.setIsValid(true);
            this.testUserDAO.insertUser(i);
            i = this.testUserDAO.selectByUsername("i").get(0);

            friendInvitation.setReceiverUserId(i.getId());
            friendInvitation.setStatus(FriendInvitationStatus.PENDING);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set i -> a: REJECTED
            friendInvitationReverse.setSenderUserId(i.getId());
            friendInvitationReverse.setStatus(FriendInvitationStatus.REJECTED);
            friendInvitationReverse.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitationReverse);

            // set a -> j: ACCEPTED
            User j = new User();
            j.setUsername("j");
            j.setIsValid(true);
            this.testUserDAO.insertUser(j);
            j = this.testUserDAO.selectByUsername("j").get(0);

            friendInvitation.setReceiverUserId(j.getId());
            friendInvitation.setStatus(FriendInvitationStatus.ACCEPTED);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set j -> a: REJECTED
            friendInvitationReverse.setSenderUserId(j.getId());
            friendInvitationReverse.setStatus(FriendInvitationStatus.REJECTED);
            friendInvitationReverse.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitationReverse);

            // set a -> k: REJECTED
            User k = new User();
            k.setUsername("k");
            k.setIsValid(true);
            this.testUserDAO.insertUser(k);
            k = this.testUserDAO.selectByUsername("k").get(0);

            friendInvitation.setReceiverUserId(k.getId());
            friendInvitation.setStatus(FriendInvitationStatus.REJECTED);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

            // set k -> a: REJECTED
            friendInvitationReverse.setSenderUserId(k.getId());
            friendInvitationReverse.setStatus(FriendInvitationStatus.REJECTED);
            friendInvitationReverse.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitationReverse);

            // set a -> l: REJECTED
            User l = new User();
            l.setUsername("l");
            l.setIsValid(true);
            this.testUserDAO.insertUser(l);
            l = this.testUserDAO.selectByUsername("l").get(0);

            friendInvitation.setReceiverUserId(l.getId());
            friendInvitation.setStatus(FriendInvitationStatus.REJECTED);
            friendInvitation.setCreateTime(new Date());
            this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);
        }
        else{
            User sender = new User();
            sender.setUsername("sender");
            sender.setEmail("123@email.com");
            sender.setPassword("password");
            sender.setIsValid(true);
            this.testUserDAO.insertUser(sender);

            User receiver = new User();
            receiver.setUsername("receiver");
            receiver.setEmail("1234@email.com");
            receiver.setPassword("password");
            receiver.setIsValid(true);
            this.testUserDAO.insertUser(receiver);
        }
    }

    @Test
    public void testAddFriends_happyCase() throws Exception{
        User sender = this.testUserDAO.selectByUsername("sender").get(0);
        String senderLoginToken = RandomStringUtils.randomAlphabetic(64);
        this.testUserDAO.login(passwordEncoder(senderLoginToken), new Date(), "sender");

        User receiver = this.testUserDAO.selectByUsername("receiver").get(0);

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

    @Test
    public void testListPendingFriendInvitation_happyCase() throws Exception{
        String senderLoginToken = RandomStringUtils.randomAlphabetic(64);
        this.testUserDAO.login(passwordEncoder(senderLoginToken), new Date(), "a");

        this.mockMvc.perform(post("/friends/listPendingFriendInvitation")
                        .header("Login-Token", senderLoginToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].status").value("PENDING"))
                .andExpect(jsonPath("$.data[1].status").value("PENDING"));
    }

    @Test
    public void testListFriend_happyCase() throws Exception{
        String senderLoginToken = RandomStringUtils.randomAlphabetic(64);
        this.testUserDAO.login(passwordEncoder(senderLoginToken), new Date(), "a");

        this.mockMvc.perform(post("/friends/listFriends")
                        .header("Login-Token", senderLoginToken))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.data.length()").value(5));
    }

    @Test
    public void testAcceptInvitation_happyCase() throws Exception{
        FriendInvitation friendInvitation = new FriendInvitation();

        User sender = this.testUserDAO.selectByUsername("sender").get(0);
        friendInvitation.setSenderUserId(sender.getId());

        User receiver = this.testUserDAO.selectByUsername("receiver").get(0);
        String senderLoginToken = RandomStringUtils.randomAlphabetic(64);
        this.testUserDAO.login(passwordEncoder(senderLoginToken), new Date(), "receiver");

        friendInvitation.setReceiverUserId(receiver.getId());
        friendInvitation.setStatus(FriendInvitationStatus.PENDING);
        friendInvitation.setCreateTime(new Date());
        this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

        this.mockMvc.perform(post("/friends/acceptInvitation")
                        .header("Login-Token", senderLoginToken)
                        .param("senderUserId", Integer.toString(sender.getId())))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        friendInvitation = this.testFriendInvitationDAO.selectBySenderUserId(sender.getId());
        assertEquals(FriendInvitationStatus.ACCEPTED, friendInvitation.getStatus());
        assertEquals(Date.class, friendInvitation.getAcceptTime().getClass());
    }

    @Test
    public void testRejectInvitation_happyCase() throws Exception{
        FriendInvitation friendInvitation = new FriendInvitation();

        User sender = this.testUserDAO.selectByUsername("sender").get(0);
        friendInvitation.setSenderUserId(sender.getId());

        User receiver = this.testUserDAO.selectByUsername("receiver").get(0);
        String senderLoginToken = RandomStringUtils.randomAlphabetic(64);
        this.testUserDAO.login(passwordEncoder(senderLoginToken), new Date(), "receiver");

        friendInvitation.setReceiverUserId(receiver.getId());
        friendInvitation.setStatus(FriendInvitationStatus.PENDING);
        friendInvitation.setCreateTime(new Date());
        this.testFriendInvitationDAO.insertFriendInvitation(friendInvitation);

        this.mockMvc.perform(post("/friends/rejectInvitation")
                        .header("Login-Token", senderLoginToken)
                        .param("senderUserId", Integer.toString(sender.getId())))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        friendInvitation = this.testFriendInvitationDAO.selectBySenderUserId(sender.getId());
        assertEquals(FriendInvitationStatus.REJECTED, friendInvitation.getStatus());
        assertEquals(Date.class, friendInvitation.getAcceptTime().getClass());
    }
}
