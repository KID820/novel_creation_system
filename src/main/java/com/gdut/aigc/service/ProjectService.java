package com.gdut.aigc.service;

import java.util.Map;

public interface ProjectService {
    
    // 获取用户的项目信息
    Map<String, Object> getProjectByUserId(Long userId);
    
    // 更新项目信息
    Map<String, Object> updateProject(Long projectId, Map<String, Object> projectData);
    
    // 创建新项目
    Map<String, Object> createProject(Long userId, Map<String, Object> projectData);
}