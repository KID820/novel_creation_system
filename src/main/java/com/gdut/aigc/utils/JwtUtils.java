package com.gdut.aigc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET_KEY = "your-secret-key-here-which-should-be-at-least-32-characters-long"; // 实际应用中应该从配置文件读取
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24小时

    // 生成JWT令牌
    public static String generateJwt(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 解析JWT令牌
    public static Claims parseJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 验证令牌是否有效
    public static boolean validateJwt(String token) {
        try {
            parseJwt(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}