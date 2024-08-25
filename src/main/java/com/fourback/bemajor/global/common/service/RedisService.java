package com.fourback.bemajor.global.common.service;

import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
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

    public void setValue(RedisKeyPrefixEnum prefixEnum, Long id,
                         String value, long expiredTime) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getDescription() + id,
                value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void deleteValue(RedisKeyPrefixEnum prefixEnum, Long id) {
        stringRedisTemplate.delete(prefixEnum.getDescription() + id);
    }

    public String getValue(RedisKeyPrefixEnum prefixEnum, Long id) {
        return stringRedisTemplate.opsForValue().get(prefixEnum.getDescription() + id);
    }

    public boolean checkKey(RedisKeyPrefixEnum prefixEnum, Long id) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(
                prefixEnum.getDescription() + id));
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
}
