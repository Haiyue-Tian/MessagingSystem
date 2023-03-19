package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.UserValidationCode;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserValidationCodeDAO {

    @Insert("INSERT INTO user_validation_code (user_id, validation_code) VALUES (#{userId}, #{validationCode})")
    void insert(UserValidationCode userValidationCode);

    @Select("SELECT validation_code FROM user_validation_code WHERE user_id = #{userId}")
    String selectValidationCodeByUserId(int userId);

    @Delete("Delete FROM user_validation_code WHERE user_id = #{userId}")
    void deleteByUserId(int userId);
}
