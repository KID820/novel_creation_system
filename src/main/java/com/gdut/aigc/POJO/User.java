package com.gdut.aigc.POJO;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    private String nickname;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String password;
    
    private String avatar;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}