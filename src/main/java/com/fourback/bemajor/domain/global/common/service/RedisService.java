package com.fourback.bemajor.domain.global.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public void setRefreshToken(String key, String value, Long expiredTime) {
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String key) {

        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }

    public void putDisConnectUser(String studdyGroupId, String oauth2Id, String fcmToken) {
        redisTemplate.opsForHash().put(studdyGroupId, oauth2Id, fcmToken);
    }

    public Map<String, String> getDisConnectUser(String studdyGroupId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        return opsForHash.entries(studdyGroupId);
    }

    public void deleteDisConnectUser(String studdyGroupId, String oauth2Id) {
        redisTemplate.opsForHash().delete(studdyGroupId, oauth2Id);
    }

    public void putFcmToken(String oauth2Id, String fcmToken) {
        redisTemplate.opsForValue().set("fcm"+oauth2Id, fcmToken);
    }

    public String getFcmToken(String oauth2Id) {
        return redisTemplate.opsForValue().get("fcm"+oauth2Id);
    }

    public void deleteFcmToken(String oauth2Id) {
        redisTemplate.delete("fcm"+oauth2Id);
    }
}
