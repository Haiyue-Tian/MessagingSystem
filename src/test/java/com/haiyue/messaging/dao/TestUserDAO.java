package com.haiyue.messaging.dao;

import com.haiyue.messaging.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestUserDAO {

    @Delete("DELETE FROM user")
    void deleteAll();

    @Select("SELECT * FROM user WHERE username = #{username}")
    List<User> selectByUsername(String username);



}
