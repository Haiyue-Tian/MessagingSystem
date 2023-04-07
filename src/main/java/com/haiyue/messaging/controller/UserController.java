package com.haiyue.messaging.controller;

import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.request.ActivateUserRequest;
import com.haiyue.messaging.request.LoginUserRequest;
import com.haiyue.messaging.request.RegisterUserRequest;
import com.haiyue.messaging.response.CommonResponse;
import com.haiyue.messaging.response.ListUsersResponse;
import com.haiyue.messaging.response.LoginUserResponse;
import com.haiyue.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/login")
    public CommonResponse login(@RequestBody LoginUserRequest loginUserRequest) throws MessageServiceException{
        String loginToken = this.userService.login(
                loginUserRequest.getIdentification(),
                loginUserRequest.getPassword());
        return new LoginUserResponse(loginToken);
    }

    @GetMapping("/search")
    public CommonResponse search(@RequestParam(value = "keyword") String keyword,
                                 @RequestParam(required = false, defaultValue = "1") int page){
        List<User> users = this.userService.search(keyword, page);
        return new ListUsersResponse(users);
    }
}