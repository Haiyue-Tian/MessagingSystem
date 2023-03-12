package com.haiyue.messaging.controller;

import com.haiyue.messaging.dao.UserDAO;
import com.haiyue.messaging.request.RegisterUserRequest;
import com.haiyue.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterUserRequest registerUserRequest) throws Exception{
        this.userService.registerUser(
                registerUserRequest.getUsername(),
                registerUserRequest.getNickname(),
                registerUserRequest.getPassword(),
                registerUserRequest.getRepeatPassword(),
                registerUserRequest.getAddress(),
                registerUserRequest.getGender(),
                registerUserRequest.getEmail());
    }
}