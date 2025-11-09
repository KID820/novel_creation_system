package com.gdut.aigc.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RememberMeStore {
    private static final Map<String, RememberEntry> STORE = new ConcurrentHashMap<>();
    // 7天
    private static final long EXPIRE = 7*24*60*60*1000L;

    public static void put(String token, Long userId) {
        STORE.put(token, new RememberEntry(userId, System.currentTimeMillis() + EXPIRE));
    }
    public static Long getUserIdIfValid(String token) {
        RememberEntry entry = STORE.get(token);
        if (entry == null) return null;
        if (System.currentTimeMillis() > entry.expireTime) {
            STORE.remove(token);
            return null;
        }
        return entry.userId;
    }
    static class RememberEntry {
        Long userId;
        long expireTime;
        RememberEntry(Long userId, long expireTime) {
            this.userId = userId;
            this.expireTime = expireTime;
        }
    }
}