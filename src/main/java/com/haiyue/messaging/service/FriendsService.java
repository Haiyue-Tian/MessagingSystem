package com.haiyue.messaging.service;

import com.haiyue.messaging.dao.FriendInvitationDAO;
import com.haiyue.messaging.dao.UserDAO;
import com.haiyue.messaging.enums.FriendInvitationStatus;
import com.haiyue.messaging.enums.Status;
import com.haiyue.messaging.exception.MessageServiceException;
import com.haiyue.messaging.model.FriendInvitation;
import com.haiyue.messaging.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FriendsService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private FriendInvitationDAO friendInvitationDAO;

    public void addFriends(User user, int receiverUserId, String message) throws MessageServiceException {
        User receiverUser = this.userDAO.selectById(receiverUserId);
        if (receiverUser == null){
            throw new MessageServiceException(Status.USER_NOT_EXISTS);
        }
        FriendInvitation friendInvitation = new FriendInvitation();
        friendInvitation.setSenderUserId(user.getId());
        friendInvitation.setReceiverUserId(receiverUserId);
        friendInvitation.setMessage(message);
        friendInvitation.setStatus(FriendInvitationStatus.PENDING);
        friendInvitation.setCreateTime(new Date());

        this.friendInvitationDAO.insertInvitation(friendInvitation);
    }
}
