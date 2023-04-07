package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.FriendInvitation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestFriendInvitationDAO {
    @Select("SELECT * FROM friend_invitation WHERE sender_user_id=#{senderUserId}")
    FriendInvitation selectBySenderUserId(int senderUserId);

    @Delete("DELETE FROM friend_invitation")
    void deleteAll();
}
