package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

// data access object
@Repository
@Mapper
public interface UserDAO {
    @Insert("INSERT INTO user (username, nickname, password, address, gender, email, is_valid, register_time)" +
            "VALUES (#{username}, #{nickname}, #{password}, #{address}, #{gender}, #{email}, #{isValid}, #{registerTime})")
    void insertUser(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectUser(int id);
}

