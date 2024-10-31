package com.fourback.bemajor.global.common.service;

import com.fourback.bemajor.global.common.enums.ExpiredTimeEnum;
import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
public class RedisService {
    //allKeys-lru 메모리 전략 사용
    private final StringRedisTemplate stringRedisTemplate;

    public void setValueWithExpiredTime(RedisKeyPrefixEnum prefixEnum, Long id, String value, ExpiredTimeEnum expiredTimeEnum) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getKeyPrefix() + id,
                value, expiredTimeEnum.getExpiredTime(), TimeUnit.MILLISECONDS);
    }

    public void setValueWithExpiredTime(RedisKeyPrefixEnum prefixEnum, Long id, String value, long expiredTime) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getKeyPrefix() + id, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void extendExpiration(RedisKeyPrefixEnum prefixEnum, Long id, ExpiredTimeEnum expiredTimeEnum) {
        stringRedisTemplate.expire(prefixEnum.getKeyPrefix() + id, expiredTimeEnum.getExpiredTime(), TimeUnit.MILLISECONDS);
    }

    public void deleteKey(RedisKeyPrefixEnum prefixEnum, Long id) {
        stringRedisTemplate.delete(prefixEnum.getKeyPrefix() + id);
    }

    public String getValue(RedisKeyPrefixEnum prefixEnum, Long id) {
        return stringRedisTemplate.opsForValue().get(prefixEnum.getKeyPrefix() + id);
    }

    public boolean hasKey(RedisKeyPrefixEnum prefixEnum, Long id) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(prefixEnum.getKeyPrefix() + id));
    }

    public void putField(RedisKeyPrefixEnum prefixEnum, Long keyId, Long valueId, Boolean isAlarmSet) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        operations.put(prefixEnum.getKeyPrefix() + keyId, valueId.toString(), isAlarmSet.toString());
    }

    public void putFields(RedisKeyPrefixEnum prefixEnum, Long keyId, Map<String, String> values) {
        if (values != null && !values.isEmpty()) {
            HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
            operations.putAll(prefixEnum.getKeyPrefix() + keyId, values);
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

        String key = prefixEnum.getKeyPrefix() + keyId;
        String field = valueId.toString();
        String value = isAlarmSet.toString();

        stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, String.class), singletonList(key), field, value);
    }

    public Map<String, String> getEntries(RedisKeyPrefixEnum prefixEnum, Long id) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        return operations.entries(prefixEnum.getKeyPrefix() + id);
    }

    public void deleteField(RedisKeyPrefixEnum prefixEnum, Long keyId, Long valueId) {
        HashOperations<String, String, String> operation = stringRedisTemplate.opsForHash();
        operation.delete(prefixEnum.getKeyPrefix() + keyId, valueId.toString());
    }

    public void deleteFieldInKeys(RedisKeyPrefixEnum keyPrefixEnum, List<Long> keyIds, Long valueId) {
        byte[] byteValueId = valueId.toString().getBytes();
        List<byte[]> byteFilteredDisConnectedKeys = keyIds.stream()
            .map(keyId -> (keyPrefixEnum.getKeyPrefix() + keyId).getBytes()).toList();

        int batchSize = 100;
        for (int start = 0; start < byteFilteredDisConnectedKeys.size(); start += batchSize) {
            int end = Math.min(start + batchSize, byteFilteredDisConnectedKeys.size());

            List<byte[]> tempByteKeys = byteFilteredDisConnectedKeys.subList(start, end);

            stringRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
                tempByteKeys.forEach(tempByteKey -> redisConnection.hashCommands().hDel(tempByteKey, byteValueId));
                return null;
            });
        }
    }
}
