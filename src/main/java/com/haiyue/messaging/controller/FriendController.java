package com.haiyue.messaging.controller;

import com.haiyue.messaging.annotation.NeedAuth;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.model.FriendInvitation;
import com.haiyue.messaging.model.User;
import com.haiyue.messaging.request.AddFriendRequest;
import com.haiyue.messaging.response.CommonResponse;
import com.haiyue.messaging.response.PaginatedResponse;
import com.haiyue.messaging.response.UserResponse;
import com.haiyue.messaging.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendsService friendsService;

    @PostMapping("/add")
    @NeedAuth
    public CommonResponse add(User user, @RequestBody AddFriendRequest addFriendRequest) throws MessageServiceException {
        this.friendsService.addFriends(
                user,
                addFriendRequest.getReceiverUserId(),
                addFriendRequest.getMessage());
        return new CommonResponse(Status.OK);
    }

    @PostMapping("/listPendingFriendInvitation")
    @NeedAuth
    public PaginatedResponse<FriendInvitation> listPendingFriendInvitation(
            User user, @RequestParam(required = false, defaultValue = "1") int page){
        List<FriendInvitation> friendInvitations = this.friendsService.listPendingFriendInvitation(user, page);
        return new PaginatedResponse<>(friendInvitations, page);
    }

    @PostMapping("/listFriends")
    @NeedAuth
    public PaginatedResponse<UserResponse> listFriends(
            User user, @RequestParam(required = false, defaultValue = "1") int page){
        List<User> users = this.friendsService.listFriend(user, page);
        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::from)
                .toList();
        return new PaginatedResponse<>(userResponses, page);
    }

    @PostMapping("/acceptInvitation")
    @NeedAuth
    public CommonResponse acceptInvitation(User user, @RequestParam int senderUserId) throws MessageServiceException{
        this.friendsService.acceptInvitation(user, senderUserId);
        return new CommonResponse(Status.OK);
    }

    @PostMapping("/rejectInvitation")
    @NeedAuth
    public CommonResponse rejectInvitation(User user, @RequestParam int senderUserId) throws MessageServiceException {
        this.friendsService.rejectInvitation(user, senderUserId);
        return new CommonResponse(Status.OK);
    }
}
