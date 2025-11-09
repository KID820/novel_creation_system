package com.gdut.aigc.controller;

import com.gdut.aigc.POJO.Result;
import com.gdut.aigc.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/project")
public class ProjectController {
    
    // 获取项目信息接口
    @GetMapping
    public Result getProject(@RequestHeader(required = false) String authorization) {
        log.info("获取项目信息");
        
        // 简单的token验证
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.error("请先登录");
        }
        
        String token = authorization.substring(7);
        if (!JwtUtils.validateJwt(token)) {
            return Result.error("登录已过期，请重新登录");
        }
        
        // 模拟获取项目信息成功
        Map<String, Object> project = new HashMap<>();
        project.put("projectId", 1);
        project.put("name", "星际迷航：新纪元");
        project.put("genre", "科幻小说");
        project.put("target", 80000);
        project.put("current", 12450);
        project.put("status", "writing");
        project.put("createdAt", LocalDateTime.now().minusDays(30).toString());
        project.put("updatedAt", LocalDateTime.now().toString());
        
        return Result.success(project);
    }
}