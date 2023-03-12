package com.haiyue.messaging.service;

import com.haiyue.messaging.dao.UserDAO;
import com.haiyue.messaging.enums.Gender;
import com.haiyue.messaging.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.haiyue.messaging.utils.Password.passwordEncoder;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    public void registerUser(String username,
                             String nickname,
                             String password,
                             String repeatPassword,
                             String address,
                             Gender gender,
                             String email) throws Exception {
        // validations
        if (!password.equals(repeatPassword)) {
            throw new Exception();
        }
        if (password.length() < 8) {
            throw new Exception();
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
    }
}
