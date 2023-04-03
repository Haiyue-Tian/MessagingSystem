package com.haiyue.messaging.controller;

import com.haiyue.messaging.annotation.NeedAuth;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.request.AddFriendRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class FriendController {
    @PostMapping("/add")
    @NeedAuth
    public void create(User user, AddFriendRequest addFriendRequest){

    }
}
