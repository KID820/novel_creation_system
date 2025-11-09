package com.gdut.aigc.mapper;

import com.gdut.aigc.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends JpaRepository<User, Long> {
    
    // 根据邮箱查找用户
    User findByEmail(String email);
    
    // 检查邮箱是否已存在
    boolean existsByEmail(String email);
}