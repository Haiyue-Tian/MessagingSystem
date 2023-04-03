package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.UserValidationCode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestUserValidationCodeDAO {

    @Delete("DELETE FROM user_validation_code")
    void deleteAll();

    @Select("SELECT * FROM user_validation_code WHERE user_id = #{userId}")
    UserValidationCode selectByUserId(int userId);

    @Insert("INSERT INTO user_validation_code (user_id, validation_code) VALUES (#{userId}, #{validationCode})")
    void insert(UserValidationCode userValidationCode);
}