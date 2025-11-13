package com.gdut.aigc.service;

import com.gdut.aigc.POJO.User;

public interface UserService {
    
    // 用户注册
    boolean register(User user);
    
    // 用户注册（重载方法）
    User register(String nickname, String email, String password);
    
    // 用户登录
    User login(String email, String password);
    
    // 根据用户ID获取用户信息
    User getUserById(Long userId);
    
    // 根据邮箱获取用户
    User getUserByEmail(String email);
}