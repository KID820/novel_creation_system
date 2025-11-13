package com.gdut.aigc.mapper;

import com.gdut.aigc.POJO.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    
    // 登录方法
    @Select("select * from users where email=#{email} and password=#{password}")
    User getByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    
    // 自动登录方法
    @Select("select * from users where user_id=#{userId}")
    User getById(Long userId);
    
    // 查重方法（查邮箱）
    @Select("select * from users where email=#{email}")
    User getByEmail(String email);
    
    // 注册方法
    @Insert("insert into users(nickname, email, password) values(#{nickname}, #{email}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int registerUser(User user);
}