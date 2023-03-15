package com.haiyue.messaging.service;

import com.haiyue.messaging.dao.UserDAO;
import com.haiyue.messaging.dao.UserValidationCodeDAO;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.request.RegisterUserRequest;
import com.haiyue.messaging.request.ValidationCodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivateService {
    @Autowired
    private UserValidationCodeDAO userValidationCodeDAO;
    @Autowired
    private UserDAO userDAO;
    public void activate(int id, String validationCode) throws MessageServiceException {
        if (!validationCode.equals(this.userValidationCodeDAO.selectById(id))){
            throw new MessageServiceException(Status.VALIDATION_CODE_NOT_MATCHED);
        }
        this.userDAO.updateIsValid(id);
        this.userValidationCodeDAO.updateById(id);
    }
}
