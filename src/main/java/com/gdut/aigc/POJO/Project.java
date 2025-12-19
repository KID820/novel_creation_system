package com.gdut.aigc.POJO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Project {
    private Long projectId;
    
    private Long userId;
    
    private String name;
    
    private String genre;
    
    private Integer target;
    
    private Integer current;
    
    private String status; // writing-写作中, paused-暂停, completed-已完成
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}