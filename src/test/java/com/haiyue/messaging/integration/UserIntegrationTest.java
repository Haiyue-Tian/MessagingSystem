package com.haiyue.messaging.integration;

// integration testing
// unit testing

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.haiyue.messaging.dao.TestUserDAO;
import com.haiyue.messaging.dao.TestUserValidationCodeDAO;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.model.UserValidationCode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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

    @BeforeEach
    public void cleanUpData() {
        this.testUserDAO.deleteAll();
        this.testUserValidationCodeDAO.deleteAll();
    }

    @Test // test+target_scenario_expectation
    public void testRegister_happyCase_oneUserRegistered() throws Exception {
        var requestBody = "{\n"
                + "    \"username\": \"xxx222dfdfddddddf12\",\n"
                + "    \"nickname\": \"xxxxdfxxxxxxx\",\n"
                + "    \"password\": \"xxxxxxxxxxx\",\n"
                + "    \"repeatPassword\": \"xxxxxxxxxxx\",\n"
                + "    \"address\": \"xxxxx\",\n"
                + "    \"gender\": \"MALE\",\n"
                + "    \"email\": \"ffffddddd2d1@343434ff.com\"\n"
                + "}";

        this.mockMvc.perform(post("/users/register")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // status code == 200
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("Success"));

        List<User> users = this.testUserDAO.selectByUsername("xxx222dfdfddddddf12");
        assertEquals(1, users.size());
        User user = users.get(0);
        assertEquals("xxx222dfdfddddddf12", user.getUsername());
        assertEquals("ffffddddd2d1@343434ff.com", user.getEmail());
        assertFalse(user.getIsValid());
        //...

        UserValidationCode userValidationCode = this.testUserValidationCodeDAO.selectByUserId(user.getId());
        assertEquals(user.getId(), userValidationCode.getUserId());
        assertEquals(6, userValidationCode.getValidationCode().length());
    }

    @Test
    public void testRegister_passwordsNotMatched_returnsBadRequest() throws Exception {

        var requestBody = "{\n"
                + "    \"username\": \"xxx222dfdfddddddf12\",\n"
                + "    \"nickname\": \"xxxxdfxxxxxxx\",\n"
                + "    \"password\": \"xxxxxxxxxxx\",\n"
                + "    \"repeatPassword\": \"xx\",\n"
                + "    \"address\": \"xxxxx\",\n"
                + "    \"gender\": \"MALE\",\n"
                + "    \"email\": \"ffffddddd2d1@343434ff.com\"\n"
                + "}";

        this.mockMvc.perform(post("/users/register")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // status code == 200
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.code").value(1001))
                .andExpect(jsonPath("$.message").value("Passwords are not matched"));
    }

    @Test
    public void testActivate() {

        // this.testUserDAO.insert();
        //

    }


}
