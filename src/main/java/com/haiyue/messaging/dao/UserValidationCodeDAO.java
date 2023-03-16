package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.UserValidationCode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserValidationCodeDAO {

    @Insert("INSERT INTO user_validation_code (username, validation_code) VALUES (#{username}, #{validationCode})")
    void insert(UserValidationCode userValidationCode);

    @Select("SELECT validation_code FROM user_validation_code WHERE username = #{username}")
    String selectByUsername(String username);

    @Update("UPDATE user_validation_code SET validation_code = null WHERE username = #{username}")
    void updateByUsername(String username);
}
