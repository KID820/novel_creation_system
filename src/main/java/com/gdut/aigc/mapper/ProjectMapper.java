package com.gdut.aigc.mapper;

import com.gdut.aigc.POJO.Project;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProjectMapper {
    
    // 根据用户ID查找项目
    @Select("SELECT * FROM projects WHERE user_id = #{userId}")
    Project findByUserId(Long userId);
    
    // 检查用户是否已有项目
    @Select("SELECT COUNT(*) > 0 FROM projects WHERE user_id = #{userId}")
    boolean existsByUserId(Long userId);
    
    // 保存项目
    @Insert("INSERT INTO projects (user_id, name, genre, target, current, status, created_at, updated_at) " +
            "VALUES (#{userId}, #{name}, #{genre}, #{target}, #{current}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "projectId")
    void save(Project project);
    
    // 更新项目
    @Update("UPDATE projects SET name = #{name}, genre = #{genre}, target = #{target}, " +
            "current = #{current}, status = #{status}, updated_at = #{updatedAt} " +
            "WHERE project_id = #{projectId}")
    void update(Project project);
}