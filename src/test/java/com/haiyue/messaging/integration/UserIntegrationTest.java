package com.haiyue.messaging.integration;

import com.haiyue.messaging.dao.TestUserDAO;
import com.haiyue.messaging.dao.TestUserValidationCodeDAO;
import com.haiyue.messaging.enums.Gender;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.model.UserValidationCode;
import com.haiyue.messaging.utils.Password;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// integration testing
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUserDAO testUserDAO;

    @Autowired
    private TestUserValidationCodeDAO testUserValidationCodeDAO;

    @Autowired
    private CleanUpUtils cleanUpUtils;

    @BeforeEach
    public void cleanUpData(){
        this.cleanUpUtils.cleanUpData();
    }

    // test+target_scenario_expectation
    @Test
    public void testRegister_happyCase_oneUserRegistered() throws Exception{
        var requestBody = "{\n" +
                "    \"username\": \"xx\",\n" +
                "    \"nickname\": \"xx\",\n" +
                "    \"password\": \"xxxxxxxx\",\n" +
                "    \"repeatPassword\": \"xxxxxxxx\",\n" +
                "    \"address\": \"xx\",\n" +
                "    \"gender\": \"MALE\",\n" +
                "    \"email\": \"er.haiyue.tian@gmail.com\"\n" +
                "}";
        this.mockMvc.perform(post("/users/register")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // status code == 200
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        List<User> users = this.testUserDAO.selectByUsername("xx");
        assertEquals(1, users.size());
        User user = users.get(0);
        assertEquals("xx", user.getUsername());
        assertEquals("xx", user.getNickname());
        assertEquals(Password.passwordEncoder("xxxxxxxx"), user.getPassword());
        assertEquals("xx", user.getAddress());
        assertEquals(Gender.MALE, user.getGender());
        assertEquals("er.haiyue.tian@gmail.com", user.getEmail());

        UserValidationCode userValidationCode = this.testUserValidationCodeDAO.selectByUserId(user.getId());
        assertEquals(user.getId(), userValidationCode.getUserId());
        assertEquals(6, userValidationCode.getValidationCode().length());
    }

    @Test
    public void testRegister_passwordNotMatched_returnBadRequest() throws Exception{
        var requestBody = "{\n" +
                "    \"username\": \"xx\",\n" +
                "    \"nickname\": \"xx\",\n" +
                "    \"password\": \"12345678\",\n" +
                "    \"repeatPassword\": \"123456789\",\n" +
                "    \"address\": \"xx\",\n" +
                "    \"gender\": \"MALE\",\n" +
                "    \"email\": \"er.haiyue.tian@gmail.com\"\n" +
                "}";
        this.mockMvc.perform(post("/users/register")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // status code == 400
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1001))
                .andExpect(jsonPath("$.message").value("Passwords are not matched"));
    }

    @Test
    public void testActivate_happyCase_oneUserActivated() throws Exception{
        User user = new User();
        user.setUsername("xx");
        user.setIsValid(false);
        this.testUserDAO.insertUser(user);
        var userValidationCode = new UserValidationCode();
        userValidationCode.setValidationCode(RandomStringUtils.randomNumeric(6));
        userValidationCode.setUserId(user.getId());
        this.testUserValidationCodeDAO.insert(userValidationCode);

        var requestBody = "{\n" +
                "    \"username\": \"" + user.getUsername() + "\",\n" +
                "    \"validationCode\": \"" + userValidationCode.getValidationCode() + "\"\n" +
                "}";
        this.mockMvc.perform(post("/users/activate")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        assertEquals(null, this.testUserValidationCodeDAO.selectByUserId(user.getId()));
    }

    @Test
    public void testActivate_usernameNotFound_returnBadRequest() throws Exception{
        User user = new User();
        user.setUsername("xx");
        user.setIsValid(false);
        this.testUserDAO.insertUser(user);
        var userValidationCode = new UserValidationCode();
        userValidationCode.setValidationCode(RandomStringUtils.randomNumeric(6));
        userValidationCode.setUserId(user.getId());
        this.testUserValidationCodeDAO.insert(userValidationCode);

        var requestBody = "{\n" +
                "    \"username\": \"x\",\n" +
                "    \"validationCode\": \"" + userValidationCode.getValidationCode() + "\"\n" +
                "}";
        this.mockMvc.perform(post("/users/activate")
                        .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(header().string("Content-Type", "application/json"))
            .andExpect(jsonPath("$.code").value(1007))
            .andExpect(jsonPath("$.message").value("Username not found"));
    }

    @Test
    public void testActivate_validationCodeNotMatched_returnBadRequest() throws Exception{
        User user = new User();
        user.setUsername("xx");
        user.setIsValid(false);
        this.testUserDAO.insertUser(user);
        var userValidationCode = new UserValidationCode();
        userValidationCode.setValidationCode(RandomStringUtils.randomNumeric(6));
        userValidationCode.setUserId(user.getId());
        this.testUserValidationCodeDAO.insert(userValidationCode);

        var requestBody = "{\n" +
                "    \"username\": \"xx\",\n" +
                "    \"validationCode\": \"" + String.valueOf(Integer.parseInt(userValidationCode.getValidationCode()) + 1) + "\"\n" +
                "}";
        this.mockMvc.perform(post("/users/activate")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1006));
    }

    @Test
    public void testLogin_happyCase() throws Exception{
        User user = new User();
        user.setUsername("username");
        user.setEmail("123@email.com");
        user.setPassword("password");
        user.setIsValid(true);
        this.testUserDAO.insertUser(user);

        var requestBody = "{\n" +
                "    \"identification\": \"username\",\n" +
                "    \"password\": \"password\"\n" +
                "}";
        this.mockMvc.perform(post("/users/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        requestBody = "{\n" +
                "    \"identification\": \"123@email.com\",\n" +
                "    \"password\": \"password\"\n" +
                "}";
        this.mockMvc.perform(post("/users/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));
    }
}
