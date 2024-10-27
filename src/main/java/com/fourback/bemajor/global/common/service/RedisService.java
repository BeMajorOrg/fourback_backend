package com.fourback.bemajor.global.common.service;

import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Long> stringLongRedisTemplate;

    public void setValueWithExpiredTime(RedisKeyPrefixEnum prefixEnum, Long id,
                                        String value, long expiredTime) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getDescription() + id,
                value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void setValue(RedisKeyPrefixEnum prefixEnum, Long id, String value) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getDescription() + id, value);
    }

    public void deleteKey(RedisKeyPrefixEnum prefixEnum, Long id) {
        stringRedisTemplate.delete(prefixEnum.getDescription() + id);
    }

    public String getValue(RedisKeyPrefixEnum prefixEnum, Long id) {
        return stringRedisTemplate.opsForValue().get(prefixEnum.getDescription() + id);
    }

    public boolean hasKey(RedisKeyPrefixEnum prefixEnum, Long id) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(prefixEnum.getDescription() + id));
    }

    public void putLongBooleanField(RedisKeyPrefixEnum prefixEnum,
                                    Long keyId, Long valueId, Boolean isAlarmSet) {
        HashOperations<String, Long, Boolean> operations = stringLongRedisTemplate.opsForHash();
        operations.put(prefixEnum.getDescription() + keyId, valueId, isAlarmSet);
    }

    public void putLongBooleanFields(RedisKeyPrefixEnum prefixEnum,
                                     Long keyId, Map<Long, Boolean> values) {
        if (values != null && values.isEmpty()) {
            HashOperations<String, Long, Boolean> operations = stringLongRedisTemplate.opsForHash();
            operations.putAll(prefixEnum.getDescription() + keyId, values);
        }
    }

    public Map<Long, Boolean> getEntriesLongBoolean(RedisKeyPrefixEnum prefixEnum, Long id) {
        HashOperations<String, Long, Boolean> operations = stringLongRedisTemplate.opsForHash();
        return operations.entries(prefixEnum.getDescription() + id);
    }

    public void deleteLongBooleanField(RedisKeyPrefixEnum prefixEnum, Long keyId, Long valueId) {
        HashOperations<String, Long, Boolean> operation = stringLongRedisTemplate.opsForHash();
        operation.delete(prefixEnum.getDescription() + keyId, valueId);
    }
}
