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
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Long> stringLongRedisTemplate;
    private final Map<Long, Set<WebSocketSession>> studyGrupIdSessionsMap;

    public void setValueWithExpiredTime(RedisKeyPrefixEnum prefixEnum, Long id,
                                        String value, long expiredTime) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getDescription() + id,
                value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void setValue(RedisKeyPrefixEnum prefixEnum,
                         Long id, String value) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getDescription() + id, value);
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

    public void addLongMember(RedisKeyPrefixEnum prefixEnum,
                              Long keyId, Long valueId) {
        stringLongRedisTemplate.opsForSet().add(
                prefixEnum.getDescription() + keyId, valueId);
    }

    public void addLongMembers(RedisKeyPrefixEnum prefixEnum,
                               Long keyId, Long[] valueIds) {
        stringLongRedisTemplate.opsForSet().add(
                prefixEnum.getDescription() + keyId, valueIds);
    }

    public Set<Long> getLongMembers(RedisKeyPrefixEnum prefixEnum, Long id) {
        SetOperations<String, Long> operation = stringLongRedisTemplate.opsForSet();
        return operation.members(prefixEnum.getDescription() + id);
    }

    public void removeLongMember(RedisKeyPrefixEnum prefixEnum, Long keyId, Long valueId) {
        SetOperations<String, Long> operation = stringLongRedisTemplate.opsForSet();
        operation.remove(prefixEnum.getDescription() + keyId, valueId);
    }

    public void removeUserInKeysUsingPipeLine(RedisKeyPrefixEnum prefixEnum,
                                              List<Long> keyIds, Long valueId) {
        stringLongRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
            byte[] byteValue = ByteBuffer.allocate(Long.BYTES)
                    .putLong(valueId).array();
            keyIds.forEach(keyId -> {
                if (!studyGrupIdSessionsMap.get(keyId).isEmpty()) {
                    byte[] keyByte = (prefixEnum.getDescription() + keyId).getBytes();
                    redisConnection.setCommands().sRem(keyByte, byteValue);
                }
            });
            return null;
        });
    }

    @Scheduled(fixedDelay = 300000)
    public void removeKeysUsingPipeLine() {
        Set<String> keys = stringLongRedisTemplate.keys(
                RedisKeyPrefixEnum.DISCONNECTED + "*");
        if (keys != null) {
            List<byte[]> byteKeys = keys.stream().filter(key -> {
                Long baseKey = Long.valueOf(key.split(":")[1]);
                return studyGrupIdSessionsMap.get(baseKey).isEmpty();
            }).map(String::getBytes).toList();
            int batchSize = 100;
            int keySize = byteKeys.size();
            for (int i = 0; i < (keySize + batchSize - 1) / batchSize; i++) {
                List<byte[]> tempByteKeys = byteKeys.subList(
                        i, i += i + batchSize < keySize ? batchSize : keySize % batchSize);
                stringLongRedisTemplate.executePipelined(
                        (RedisCallback<?>) redisConnection -> {
                            tempByteKeys.forEach(key -> redisConnection.keyCommands().del(key));
                            return null;
                        });
            }
        }
    }
}
