package com.gdut.aigc.mapper;

import com.gdut.aigc.POJO.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMapper extends JpaRepository<Project, Long> {
    
    // 根据用户ID查找项目
    Project findByUserId(Long userId);
    
    // 检查用户是否已有项目
    boolean existsByUserId(Long userId);
}