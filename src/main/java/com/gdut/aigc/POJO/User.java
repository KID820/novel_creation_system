package com.gdut.aigc.POJO;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
public class User {
    private Long userId;
    
    private String nickname;
    
    private String email;
    
    private String password;
    
    private String avatar;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}