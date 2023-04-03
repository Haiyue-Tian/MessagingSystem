package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface TestUserDAO {
    @Delete("DELETE FROM user")
    void deleteAll();

    @Select("SELECT * FROM user WHERE username = #{username}")
    List<User> selectByUsername(String username);

    @Insert("INSERT INTO user (username, nickname, password, address, gender, email, is_valid, register_time)" +
            "VALUES (#{username}, #{nickname}, #{password}, #{address}, #{gender}, #{email}, #{isValid}, #{registerTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertUser(User user);

    @Update("UPDATE user SET login_token = #{loginToken}, last_login_time = #{now} WHERE username = #{username}")
    void login(String loginToken, Date now, String username);
}