package com.gdut.aigc.service.impl;

import com.gdut.aigc.POJO.User;
import com.gdut.aigc.mapper.UserMapper;
import com.gdut.aigc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(User user) {
        // 用邮箱查重
        if (userMapper.getByEmail(user.getEmail()) != null) {
            return false; // 邮箱已存在
        }
        // 设置创建时间和更新时间
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        int result = userMapper.registerUser(user); // 插入并回填userId
        return result > 0;
    }

    @Override
    public User register(String nickname, String email, String password) {
        // 用邮箱查重
        if (userMapper.getByEmail(email) != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        // 创建用户对象
        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(password); // 实际项目中应该加密
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        // 插入数据库
        int result = userMapper.registerUser(user);
        if (result > 0) {
            return user;
        } else {
            throw new RuntimeException("注册失败");
        }
    }

    @Override
    public User login(String email, String password) {
        return userMapper.getByEmailAndPassword(email, password);
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.getById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getByEmail(email);
    }
}