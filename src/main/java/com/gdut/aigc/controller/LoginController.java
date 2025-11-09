package com.gdut.aigc.controller;

import com.gdut.aigc.POJO.Result;
import com.gdut.aigc.POJO.User;
import com.gdut.aigc.service.RememberMeStore;
import com.gdut.aigc.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    // 登录方法
    @PostMapping("/api/user/login")
    public Result login(@RequestBody Map<String, String> loginRequest) {
        log.info("jinru");
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        log.info("用户登录: {}", email);
        
        // 简单的参数验证
        if (email == null || password == null) {
            return Result.error("邮箱或密码不能为空");
        }
        
        // 模拟用户验证
        if (email.equals("zhangsan@example.com") && password.equals("123456")) {
            // 模拟用户信息
            User user = new User();
            user.setUserId(1L);
            user.setNickname("张三");
            user.setEmail(email);
            
            // 生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            claims.put("nickname", user.getNickname());
            claims.put("email", user.getEmail());
            
            String jwt = JwtUtils.generateJwt(claims);
            
            // 如果需要记住我功能，可以生成记住我令牌
            String rememberToken = null;
            if ("true".equals(loginRequest.get("remember"))) {
                rememberToken = RememberMeStore.generateToken(user.getUserId());
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", jwt);
            data.put("userInfo", user);
            if (rememberToken != null) {
                data.put("remember_token", rememberToken);
            }
            
            return Result.success("登录成功", data);
        } else {
            return Result.error("邮箱或密码错误");
        }
    }

    // 自动登录接口
    @PostMapping("/api/user/autoLogin")
    public Result autoLogin(@RequestBody Map<String, String> body) {
        String rememberToken = body.get("remember_token");
        Long userId = RememberMeStore.getUserIdIfValid(rememberToken);

        if (userId == null) {
            return Result.error("自动登录已过期，请重新登录");
        }

        // 模拟通过userId查找用户
        User user = new User();
        user.setUserId(userId);
        user.setNickname("张三");
        user.setEmail("zhangsan@example.com");

        // 生成新的JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("nickname", user.getNickname());
        claims.put("email", user.getEmail());

        String jwt = JwtUtils.generateJwt(claims);

        Map<String, Object> data = new HashMap<>();
        data.put("token", jwt);
        data.put("userInfo", user);

        return Result.success("自动登录成功", data);
    }
}