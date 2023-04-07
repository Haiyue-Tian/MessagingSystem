package com.haiyue.messaging.integration;

import com.haiyue.messaging.dao.TestFriendInvitationDAO;
import com.haiyue.messaging.dao.TestUserDAO;
import com.haiyue.messaging.dao.TestUserValidationCodeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanUpUtils {

    @Autowired
    private TestUserValidationCodeDAO testUserValidationCodeDAO;

    @Autowired
    private TestUserDAO testUserDAO;

    @Autowired
    private TestFriendInvitationDAO testFriendInvitationDAO;

    public void cleanUpData(){
        this.testUserValidationCodeDAO.deleteAll();
        this.testUserDAO.deleteAll();
        this.testFriendInvitationDAO.deleteAll();
    }
}
