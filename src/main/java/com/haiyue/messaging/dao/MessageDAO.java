package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface MessageDAO {
    @Insert("INSERT INTO message (sender_user_id, group_chat_id, receiver_user_id, content, send_time, message_type)" +
            "VALUES (#{senderUserId}, #{groupChatId}, #{receiverUserId}, #{content}, #{sendTime}, #{messageType})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertMessage(Message message);
}
