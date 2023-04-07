package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

// data access object
@Repository
@Mapper
public interface UserDAO {
    @Insert("INSERT INTO user (username, nickname, password, address, gender, email, is_valid, register_time)" +
            "VALUES (#{username}, #{nickname}, #{password}, #{address}, #{gender}, #{email}, #{isValid}, #{registerTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertUser(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(int id);

    @Select("SELECT * FROM user WHERE email = #{email}")
    List<User> selectByEmail(String email);

    @Select("SELECT * FROM user WHERE username = #{username}")
    List<User> selectByUsername(String username);

    @Select("SELECT * FROM user WHERE login_token = #{loginToken}")
    User selectByLoginToken(String loginToken);

    @Update("UPDATE user SET login_token = #{loginToken}, last_login_time = #{now} WHERE id = #{id}")
    void login(String loginToken, Date now, int id);

    @Update("UPDATE user SET is_valid = 1 WHERE id = #{id}")
    void updateIsValid(int id);

    @Select("SELECT * FROM user " +
            "WHERE (username LIKE '%${keyword}%' OR nickname LIKE '%${keyword}%')" +
            "ORDER BY (" +
            "CASE " +
            "   WHEN username LIKE '%${keyword}%' AND nickname LIKE '%${keyword}%' " +
            "   THEN GREATEST(LENGTH('${keyword}') / LENGTH(username), LENGTH('${keyword}') / LENGTH(nickname))" +
            "   WHEN username LIKE '%${keyword}%' " +
            "   THEN LENGTH('${keyword}') / LENGTH(username) " +
            "   WHEN nickname LIKE '%${keyword}%' " +
            "   THEN LENGTH('${keyword}') / LENGTH(nickname) " +
            "END) DESC " +
            "LIMIT ${start}, ${limit}")
    List<User> search(String keyword, int start, int limit);
}

