package com.gdut.aigc.controller;

import com.gdut.aigc.POJO.Result;
import com.gdut.aigc.POJO.User;
import com.gdut.aigc.service.UserService;
import com.gdut.aigc.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    // 用户注册接口
    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> registerRequest) {
        String nickname = registerRequest.get("nickname");
        String email = registerRequest.get("email");
        String password = registerRequest.get("password");
        String confirm = registerRequest.get("confirm");
        
        log.info("用户注册: {}", email);
        
        // 参数验证
        if (nickname == null || email == null || password == null || confirm == null) {
            return Result.error("所有字段均为必填");
        }
        
        if (!password.equals(confirm)) {
            return Result.error("两次输入的密码不一致");
        }
        
        if (nickname.length() < 2 || nickname.length() > 10) {
            return Result.error("昵称长度应为2-10个字符");
        }
        
        if (password.length() < 6 || password.length() > 20) {
            return Result.error("密码长度应为6-20位");
        }
        
        // 调用UserService进行注册
        try {
            User user = userService.register(nickname, email, password);
            return Result.success("注册成功", user);
        } catch (Exception e) {
            log.error("注册失败", e);
            return Result.error("注册失败：" + e.getMessage());
        }
    }
    
    // 获取用户信息接口
    @GetMapping("/profile")
    public Result getProfile(@RequestHeader(required = false) String authorization) {
        log.info("获取用户信息");
        
        // 简单的token验证
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.error("请先登录");
        }
        
        String token = authorization.substring(7);
        if (!JwtUtils.validateJwt(token)) {
            return Result.error("登录已过期，请重新登录");
        }
        
        // 模拟获取用户信息成功
        User user = new User();
        user.setUserId(1L);
        user.setNickname("张三");
        user.setEmail("zhangsan@example.com");
        user.setAvatar("https://example.com/avatar.jpg");
        user.setCreatedAt(LocalDateTime.now().minusDays(30));
        
        return Result.success(user);
    }
}