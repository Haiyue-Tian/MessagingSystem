package com.haiyue.messaging.dao;

import com.haiyue.messaging.enums.MessageType;
import com.haiyue.messaging.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface MessageDAO {
    @Insert("INSERT INTO message (sender_user_id, group_chat_id, receiver_user_id, send_time, message_type)" +
            "VALUES (#{senderUserId}, #{groupChatId}, #{receiverUserId}, #{sendTime}, #{messageType})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertMessage(Message message);

    @Select("SELECT message_type FROM message WHERE id = #{messageId}")
    MessageType selectMessageTypeByMessageId(int messageId);

    @Select("SELECT * FROM message" +
            "WHERE " +
            "   (sender_user_id = #{id} AND receiver_user_id = #{receiverUserId}) OR" +
            "   (sender_user_id = #{receiverUserId} AND receiver_user_id = #{id})" +
            "ORDER BY send_time DESC " +
            "LIMIT ${start}, ${limit}")
    List<Message> selectMessages(int id, Integer groupChatId, Integer receiverUserId, int start, int i);

    @Select("SELECT * FROM message WHERE id = #{messageId}")
    Message selectMessageByMessageId(int messageId);
}
