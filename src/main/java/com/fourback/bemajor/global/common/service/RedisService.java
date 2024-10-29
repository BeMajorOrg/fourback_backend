package com.fourback.bemajor.global.common.service;

import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public void setValueWithExpiredTime(RedisKeyPrefixEnum prefixEnum, Long id,
                                        String value, long expiredTime) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getDescription() + id,
                value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void extendExpiration(RedisKeyPrefixEnum prefixEnum, Long id, long expiredTime) {
        stringRedisTemplate.expire(prefixEnum.getDescription() + id, expiredTime, TimeUnit.MILLISECONDS);
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

    public void putField(RedisKeyPrefixEnum prefixEnum,
                         Long keyId, Long valueId, Boolean isAlarmSet) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        operations.put(prefixEnum.getDescription() + keyId, valueId.toString(), isAlarmSet.toString());
    }

    public void putFields(RedisKeyPrefixEnum prefixEnum,
                          Long keyId, Map<String, String> values) {
        if (values != null && !values.isEmpty()) {
            HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
            operations.putAll(prefixEnum.getDescription() + keyId, values);
        }
    }

    public void putFieldIfPresence(RedisKeyPrefixEnum prefixEnum,
                                   Long keyId, Long valueId, Boolean isAlarmSet) {
        String luaScript =
                "local key = KEYS[1] " +
                        "local field = ARGV[1] " +
                        "local value = ARGV[2] " +
                        "if redis.call('EXISTS', key) == 1 then " +
                        "    redis.call('HSET', key, field, value) " +
                        "end";

        String key = prefixEnum.getDescription() + keyId;
        String field = valueId.toString();
        String value = isAlarmSet.toString();

        stringRedisTemplate.execute(
                new DefaultRedisScript<>(luaScript, String.class), singletonList(key), field, value);
    }

    public Map<String, String> getEntries(RedisKeyPrefixEnum prefixEnum, Long id) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        return operations.entries(prefixEnum.getDescription() + id);
    }

    public void deleteField(RedisKeyPrefixEnum prefixEnum, Long keyId, Long valueId) {
        HashOperations<String, String, String> operation = stringRedisTemplate.opsForHash();
        operation.delete(prefixEnum.getDescription() + keyId, valueId.toString());
    }
}
