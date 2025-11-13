package com.gdut.aigc.controller;

import com.gdut.aigc.POJO.Result;
import com.gdut.aigc.POJO.User;
import com.gdut.aigc.service.RememberMeStore;
import com.gdut.aigc.service.UserService;
import com.gdut.aigc.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class LoginController {

    @Autowired
    private UserService userService;



    // 登录方法
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        log.info("用户登录: {}", email);
        
        // 简单的参数验证
        if (email == null || password == null) {
            return Result.error(400, "邮箱或密码不能为空");
        }
        
        // 调用服务层进行登录验证
        User user = userService.login(email, password);
        
        // 验证用户是否存在以及密码是否正确
        if (user != null) {
            // 生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            claims.put("nickname", user.getNickname());
            claims.put("email", user.getEmail());
            
            String jwt = JwtUtils.generateJwt(claims);
            
            // 构建data对象，包含token和userInfo
            Map<String, Object> data = new HashMap<>();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("email", user.getEmail());
            
            data.put("token", jwt);
            data.put("userInfo", userInfo);
            
            return Result.success("登录成功", data);
        } else {
            return Result.error(401, "邮箱或密码错误");
        }
    }

    // 自动登录接口
    @PostMapping("/autoLogin")
    public Result autoLogin(@RequestBody Map<String, String> body) {
        String rememberToken = body.get("remember_token");
        Long userId = RememberMeStore.getUserIdIfValid(rememberToken);

        if (userId == null) {
            return Result.error(401, "自动登录已过期，请重新登录");
        }

        // 调用服务层获取用户信息
        User user = userService.getUserById(userId);
        
        if (user != null) {
            // 生成新的JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            claims.put("nickname", user.getNickname());
            claims.put("email", user.getEmail());

            String jwt = JwtUtils.generateJwt(claims);

            // 构建data对象，包含token和userInfo
            Map<String, Object> data = new HashMap<>();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("email", user.getEmail());
            
            data.put("token", jwt);
            data.put("userInfo", userInfo);
            
            return Result.success("自动登录成功", data);
        } else {
            return Result.error(401, "用户不存在");
        }
    }

}