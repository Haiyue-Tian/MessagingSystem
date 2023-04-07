package com.haiyue.messaging.controller;

import com.haiyue.messaging.annotation.NeedAuth;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.request.AddFriendRequest;
import com.haiyue.messaging.response.CommonResponse;
import com.haiyue.messaging.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendsService friendsService;

    @PostMapping("/add")
    @NeedAuth
    public CommonResponse create(User user, @RequestBody AddFriendRequest addFriendRequest) throws MessageServiceException {
        this.friendsService.addFriends(
                user,
                addFriendRequest.getReceiverUserId(),
                addFriendRequest.getMessage());
        return new CommonResponse(Status.OK);
    }
}
