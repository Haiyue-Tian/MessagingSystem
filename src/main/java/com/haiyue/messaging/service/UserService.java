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
import java.util.List;

import static com.haiyue.messaging.utils.Password.passwordEncoder;

@Service
public class UserService {
    // dependency injection
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserValidationCodeDAO userValidationCodeDAO;
    @Autowired
    private JavaMailEmailService javaMailEmailService;
    private static final long LOGIN_TOKEN_EXPIRY_IN_MS = 7 * 24 * 60 * 60 * 1000;

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

    public void activate(String username, String validationCode) throws MessageServiceException {

        List<User> list = this.userDAO.selectByUsername(username);
        if (list.isEmpty()){
            throw new MessageServiceException(Status.USERNAME_NOT_FOUND);
        }
        int id = list.get(0).getId();
        if (!validationCode.equals(this.userValidationCodeDAO.selectValidationCodeByUserId(id))){
            throw new MessageServiceException(Status.VALIDATION_CODE_NOT_MATCHED);
        }
        this.userDAO.updateIsValid(id);
        this.userValidationCodeDAO.deleteByUserId(id);
    }

    public String login(String identification, String password) throws MessageServiceException{
        User user = getUserByIdentification(identification);
        if (user == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        if (user.getPassword().equals(passwordEncoder(password))){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }

        if (!user.getIsValid()){
            throw new MessageServiceException(Status.USER_NOT_VALIDATED);
        }

        String loginToken = RandomStringUtils.randomAlphabetic(64);
        this.userDAO.login(loginToken, new Date(), user.getId());
        return loginToken;
    }

    public User authenticate(String loginToken) throws MessageServiceException{
        // authenticate -> identify who I am
        // authorization -> check if I have the permission for this resource
        // actually should use List<User> if size > 1 set loginToken as Null
        User user = this.userDAO.selectByLoginToken(loginToken);
        if (user == null){
            throw new MessageServiceException(Status.FORBIDDEN);
        }
        if (new Date().getTime() - user.getLastLoginTime().getTime() > LOGIN_TOKEN_EXPIRY_IN_MS){
            throw new MessageServiceException(Status.FORBIDDEN);
        }
        return user;
    }


    public User getUserByIdentification(String identification){
        User user = null;
        List<User> selectedUsers = this.userDAO.selectByUsername(identification);
        if (!selectedUsers.isEmpty()){
            user = selectedUsers.get(0);
        }
        if (user == null){
            selectedUsers = this.userDAO.selectByEmail(identification);
            if (!selectedUsers.isEmpty()){
                user = selectedUsers.get(0);
            }
        }
        return user;
    }

    public List<User> search(String keyword) {
        return null;
    }
}
