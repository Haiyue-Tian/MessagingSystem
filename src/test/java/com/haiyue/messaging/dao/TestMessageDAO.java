package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.Message;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestMessageDAO {
    @Delete("DELETE FROM message")
    void deleteAll();

    @Select("SELECT * FROM message WHERE sender_user_id = #{senderUserId} AND receiver_user_id = #{receiverUserId}")
    Message selectBySenderAndReceiver(int senderUserId, int receiverUserId);
}
