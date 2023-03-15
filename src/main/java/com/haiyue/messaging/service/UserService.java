package com.haiyue.messaging.service;

import com.haiyue.messaging.dao.UserDAO;
import com.haiyue.messaging.dao.UserValidationCodeDAO;
import com.haiyue.messaging.enums.Gender;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.model.UserValidationCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.haiyue.messaging.utils.Password.passwordEncoder;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserValidationCodeDAO userValidationCodeDAO;
    @Autowired
    private JavaMailEmailService javaMailEmailService;
    public void registerUser(String username,
                             String nickname,
                             String password,
                             String repeatPassword,
                             String address,
                             Gender gender,
                             String email) throws MessageServiceException{
        // validations
        if (!password.equals(repeatPassword)) {
            throw new MessageServiceException(Status.PASSWORD_NOT_MATCHED);
        }
        if (password.length() < 8) {
            throw new MessageServiceException(Status.PASSWORD_TOO_SHORT);
        }
        if (!this.userDAO.selectByEmail(email).isEmpty()){
            throw new MessageServiceException(Status.EMAIL_EXISTS);
        }
        if (!this.userDAO.selectByUsername(username).isEmpty()){
            throw new MessageServiceException(Status.USERNAME_EXISTS);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder(password));
        user.setNickname(nickname);
        user.setAddress(address);
        user.setGender(gender);
        user.setEmail(email);
        user.setRegisterTime(new Date());
        user.setIsValid(false);
        this.userDAO.insertUser(user);

        var userValidationCode = new UserValidationCode();
        userValidationCode.setValidationCode(RandomStringUtils.randomNumeric(6));
        userValidationCode.setUserId(user.getId());
        this.userValidationCodeDAO.insert(userValidationCode);

        // send email to "email"
        javaMailEmailService.sendValidationEmail(user.getEmail(), userValidationCode.getValidationCode());
    }
}
