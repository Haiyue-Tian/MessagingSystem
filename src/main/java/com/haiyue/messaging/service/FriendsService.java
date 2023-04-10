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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.haiyue.messaging.utils.Constant.PAGE_SIZE;

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
        FriendInvitationStatus status = this.friendInvitationDAO.selectStatusBySenderAndReceiver(user.getId(), receiverUserId);
        if (status == FriendInvitationStatus.PENDING || status == FriendInvitationStatus.ACCEPTED){
            throw new MessageServiceException(Status.INVITATION_NOT_EXIST_OR_EXPIRED);
        }
        if (status == FriendInvitationStatus.REJECTED){
            this.friendInvitationDAO.updateStatusFromRejectedToPending(user.getId(), receiverUserId);
        }
        FriendInvitation friendInvitation = new FriendInvitation();
        friendInvitation.setSenderUserId(user.getId());
        friendInvitation.setReceiverUserId(receiverUserId);
        friendInvitation.setMessage(message);
        friendInvitation.setStatus(FriendInvitationStatus.PENDING);
        friendInvitation.setCreateTime(new Date());

        this.friendInvitationDAO.insertInvitation(friendInvitation);
    }

    public List<FriendInvitation> listPendingFriendInvitation(User user, int page){
        int start = (page - 1) * PAGE_SIZE;
        return this.friendInvitationDAO.selectPendingFriendById(user.getId(), start, PAGE_SIZE + 1);
    }

    public List<User> listFriend(User user, int page) {
        int start = (page - 1) * PAGE_SIZE;
        List<FriendInvitation> friendInvitations = this.friendInvitationDAO.selectAcceptedFriendById(
                user.getId(), start, PAGE_SIZE + 1);
        List<User> users = new ArrayList<>();
        for (FriendInvitation inv: friendInvitations){
            users.add(inv.getSenderUserId() ==
                    user.getId()?
                    this.userDAO.selectById(inv.getReceiverUserId()): this.userDAO.selectById(inv.getSenderUserId()));
        }
        return users;
    }

    public void acceptInvitation(User user, int senderUserId) throws MessageServiceException {
        if (user.getId() == senderUserId){
            throw new MessageServiceException(Status.CANNOT_SELF_ACCEPT);
        }
        FriendInvitationStatus status = this.friendInvitationDAO.selectStatusBySenderAndReceiver(senderUserId, user.getId());
        if (status != FriendInvitationStatus.PENDING){
            throw new MessageServiceException(Status.INVITATION_NOT_EXIST_OR_EXPIRED);
        }
        status = this.friendInvitationDAO.selectStatusBySenderAndReceiver(user.getId(), senderUserId);
        if (status == FriendInvitationStatus.ACCEPTED){
            return;
        }
        this.friendInvitationDAO.updateAcceptInvitation(user.getId(), senderUserId, new Date());
    }

    public void rejectInvitation(User user, int senderUserId) throws MessageServiceException {
        if (user.getId() == senderUserId){
            throw new MessageServiceException(Status.CANNOT_SELF_REJECT);
        }
        FriendInvitationStatus status = this.friendInvitationDAO.selectStatusBySenderAndReceiver(senderUserId, user.getId());
        if (status != FriendInvitationStatus.PENDING){
            throw new MessageServiceException(Status.INVITATION_NOT_EXIST_OR_EXPIRED);
        }
        this.friendInvitationDAO.updateRejectInvitation(user.getId(), senderUserId, new Date());
    }
}
