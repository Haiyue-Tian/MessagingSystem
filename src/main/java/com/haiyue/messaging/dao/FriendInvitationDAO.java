package com.haiyue.messaging.dao;

import com.haiyue.messaging.enums.FriendInvitationStatus;
import com.haiyue.messaging.model.FriendInvitation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface FriendInvitationDAO {

    @Insert("INSERT INTO friend_invitation (sender_user_id, receiver_user_id, message, status, create_time)" +
            "VALUES (#{senderUserId}, #{receiverUserId}, #{message}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertInvitation(FriendInvitation friendInvitation);

    @Select("SELECT * FROM friend_invitation " +
            "WHERE receiver_user_id = #{id} AND status = 'PENDING' " +
            "ORDER BY create_time DESC " +
            "LIMIT ${start}, ${limit}")
    List<FriendInvitation> selectPendingFriendById(int id, int start, int limit);

    @Select("SELECT * FROM friend_invitation " +
            "WHERE (sender_user_id = #{id} OR receiver_user_id = #{id}) AND status = 'ACCEPTED' " +
            "ORDER BY create_time DESC " +
            "LIMIT ${start}, ${limit}")
    List<FriendInvitation> selectAcceptedFriendById(int id, int start, int limit);

    @Update("UPDATE friend_invitation SET status = 'ACCEPTED', accept_time = #{acceptTime} " +
            "WHERE (sender_user_id = #{senderUserId} AND receiver_user_id = #{receiverUserId}) AND status = 'PENDING'")
    void updateAcceptInvitation(int receiverUserId, int senderUserId, Date acceptTime);

    @Update("UPDATE friend_invitation SET status = 'REJECTED', accept_time = #{acceptTime} " +
            "WHERE (sender_user_id = #{senderUserId} AND receiver_user_id = #{receiverUserId}) AND status = 'PENDING'")
    void updateRejectInvitation(int receiverUserId, int senderUserId, Date acceptTime);

    @Select("SELECT status FROM friend_invitation " +
            "WHERE sender_user_id = #{senderUserId} AND receiver_user_id = #{receiverUserId}")
    FriendInvitationStatus selectStatusBySenderAndReceiver(int senderUserId, int receiverUserId);

    @Update("UPDATE friend_invitation SET status = 'PENDING', accept_date = null " +
            "WHERE (sender_user_id = #{senderUserId} AND receiver_user_id = #{receiverUserId}) AND status = 'REJECTED'")
    void updateStatusFromRejectedToPending(int senderUserId, int receiverUserId);
}
