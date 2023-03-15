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

    @Insert("INSERT INTO user_validation_code (user_id, validation_code) VALUES (#{userId}, #{validationCode})")
    void insert(UserValidationCode userValidationCode);

    @Select("SELECT validation_code FROM user_validation_code WHERE user_id = #{id}")
    String selectById(int id);

    @Update("UPDATE user_validation_code SET validation_code = null WHERE user_id = #{id}")
    void updateById(int id);
}
