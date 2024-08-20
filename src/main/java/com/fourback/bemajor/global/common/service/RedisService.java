package com.fourback.bemajor.global.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;


import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Long> stringLongRedisTemplate;
    private final Map<Long, Set<WebSocketSession>> studyGrupIdSessionsMap;

    public void setRefreshToken(Long userId, String value, long expiredTime) {
        stringRedisTemplate.opsForValue().set("Refresh:" + userId,
                value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(Long userId) {
        stringRedisTemplate.delete("Refresh:" + userId);
    }

    public boolean checkDisConnectUserKey(Long studyGroupId) {
        return Boolean.TRUE.equals(stringLongRedisTemplate.hasKey(
                "disConnectUser:" + studyGroupId));
    }

    public void putDisConnectUser(Long studyGroupId, Long userId) {
        stringLongRedisTemplate.opsForSet().add(
                "disConnectUser:" + studyGroupId, userId);
    }

    public void putDisConnectUserAll(Long studyGroupId, Long[] userIds) {
        stringLongRedisTemplate.opsForSet().add(
                "disConnectUser:" + studyGroupId, userIds);
    }

    public Set<Long> getDisConnectUser(Long studyGroupId) {
        SetOperations<String, Long> operation = stringLongRedisTemplate.opsForSet();
        return operation.members("disConnectUser:" + studyGroupId);
    }

    public void deleteUsersJoined(List<Long> studyGroupIds, Long userId) {
        stringLongRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
            byte[] byteUserId = ByteBuffer.allocate(Long.BYTES)
                    .putLong(userId).array();
            studyGroupIds.forEach(studyGroupId -> {
                if (!studyGrupIdSessionsMap.get(studyGroupId).isEmpty()) {
                    byte[] byteArray = ByteBuffer.allocate(Long.BYTES)
                            .putLong(studyGroupId).array();
                    redisConnection.setCommands().sRem(byteArray, byteUserId);
                }
            });
            return null;
        });
    }

    @Scheduled(fixedDelay = 300000, initialDelay = 300000)
    public void deleteDisConnectUserKeysInPipe() {
        Set<String> allKeys = stringLongRedisTemplate.keys("disConnectUser:*");
        List<Long> list = Objects.requireNonNull(allKeys).stream().map(key ->
                Long.valueOf(key.split(":")[1])).toList();
        stringLongRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
            list.forEach(studyGroupId -> {
                if (!studyGrupIdSessionsMap.get(studyGroupId).isEmpty()) {
                    byte[] byteArray = ByteBuffer.allocate(Long.BYTES)
                            .putLong(studyGroupId).array();
                    redisConnection.keyCommands().del(byteArray);
                }
            });
            return null;
        });
    }

    public void deleteDisConnectUser(Long studyGroupId, Long userId) {
        SetOperations<String, Long> operation = stringLongRedisTemplate.opsForSet();
        Long a = operation.remove("disConnectUser:" + studyGroupId, userId);
    }

    public void putFcmToken(Long userId, String fcmToken, long expiredTime) {
        stringRedisTemplate.opsForValue().set(
                "fcm:" + userId, fcmToken, expiredTime, TimeUnit.MILLISECONDS);
    }

    public String getFcmToken(Long userId) {
        return stringRedisTemplate.opsForValue().get("fcm:" + userId);
    }

    public void deleteFcmToken(Long userId) {
        stringRedisTemplate.delete("fcm:" + userId);
    }
}
