package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

// data access object
@Repository
@Mapper
public interface UserDAO {

    @Insert("INSERT INTO user (username, nickname, password, address, gender, email, is_valid, register_time)"
            +
            "VALUES (#{username}, #{nickname}, #{password}, #{address}, #{gender}, #{email}, #{isValid}, #{registerTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertUser(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(int id);

    @Select("SELECT * FROM user WHERE email = #{email}")
    List<User> selectByEmail(String email);

    @Select("SELECT * FROM user WHERE username = #{username}")
    List<User> selectByUsername(String username);

    @Update("UPDATE user SET is_valid = 1 WHERE id = #{id}")
    void updateIsValid(int id);

    @Delete("DELETE FROM user")
    void deleteAll();
}

