package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.FriendInvitation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FriendInvitationDAO {

    @Insert("INSERT INTO friend_invitation (sender_user_id, receiver_user_id, message, status, create_time)" +
            "VALUES (#{senderUserId}, #{receiverUserId}, #{message}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertInvitation(FriendInvitation friendInvitation);
}
