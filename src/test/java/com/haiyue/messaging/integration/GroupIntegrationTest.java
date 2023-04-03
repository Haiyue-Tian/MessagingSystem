package com.haiyue.messaging.integration;

import com.haiyue.messaging.dao.TestUserDAO;
import com.haiyue.messaging.dao.TestUserValidationCodeDAO;
import com.haiyue.messaging.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class GroupIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUserDAO testUserDAO;

    @Autowired
    private TestUserValidationCodeDAO testUserValidationCodeDAO;

    @BeforeEach
    public void cleanUpData(){
        this.testUserValidationCodeDAO.deleteAll();
        this.testUserDAO.deleteAll();
    }

    // test+target_scenario_expectation
    @Test
    public void testCreate_happyCase() throws Exception{
        User user = new User();
        user.setUsername("username");
        user.setEmail("123@email.com");
        user.setPassword("password");
        user.setIsValid(true);
        this.testUserDAO.insertUser(user);

        String loginToken = RandomStringUtils.randomAlphabetic(64);

        this.testUserDAO.login(loginToken, new Date(), "username");

        this.mockMvc.perform(post("/groups/create")
                        .header("Login-Token", loginToken))
                .andExpect(status().isOk()) // status code == 200
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));
    }
}
