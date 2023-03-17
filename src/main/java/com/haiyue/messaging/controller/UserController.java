package com.haiyue.messaging.controller;

import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.request.ActivateUserRequest;
import com.haiyue.messaging.request.RegisterUserRequest;
import com.haiyue.messaging.response.CommonResponse;
import com.haiyue.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public CommonResponse register(@RequestBody RegisterUserRequest registerUserRequest) throws MessageServiceException {
        this.userService.registerUser(
                registerUserRequest.getUsername(),
                registerUserRequest.getNickname(),
                registerUserRequest.getPassword(),
                registerUserRequest.getRepeatPassword(),
                registerUserRequest.getAddress(),
                registerUserRequest.getGender(),
                registerUserRequest.getEmail());
        return new CommonResponse(Status.OK);
    }

    @PostMapping("/activate")
    public CommonResponse activate(@RequestBody ActivateUserRequest activateUserRequest) throws MessageServiceException{
        this.userService.activate(
                activateUserRequest.getUsername(),
                activateUserRequest.getValidationCode());
        return new CommonResponse(Status.OK);
    }
}