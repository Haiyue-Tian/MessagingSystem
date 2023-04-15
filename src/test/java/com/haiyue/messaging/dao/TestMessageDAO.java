package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestMessageDAO {
    @Delete("DELETE FROM message")
    void deleteAll();

    @Select("SELECT * FROM message WHERE sender_user_id = #{senderUserId} AND receiver_user_id = #{receiverUserId}")
    Message selectBySenderAndReceiver(int senderUserId, int receiverUserId);

    @Insert("INSERT INTO message (sender_user_id, group_chat_id, receiver_user_id, send_time, message_type)" +
            "VALUES (#{senderUserId}, #{groupChatId}, #{receiverUserId}, #{sendTime}, #{messageType})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertMessage(Message message);
}
