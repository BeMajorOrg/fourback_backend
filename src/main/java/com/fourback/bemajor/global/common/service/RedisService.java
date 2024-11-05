package com.fourback.bemajor.global.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
public class RedisService {
    //allKeys-lru 메모리 전략 사용
    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Void> hsetxxScript;

    public void setValueWithExpiredTime(String key, String value, long expiredTime) {
        stringRedisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void deleteKey(String key) {
        stringRedisTemplate.delete(key);
    }

    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    public void putField(String key, String fieldKey, String fieldValue) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        operations.put(key, fieldKey, fieldValue);
    }

    public void putFieldWithExpiredTime(String key, String fieldKey, String fieldValue, long expiredTime) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        operations.put(key, fieldKey, fieldValue);
        stringRedisTemplate.expire(key, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void putFields(String key, Map<String, String> fields) {
        if (fields != null && !fields.isEmpty()) {
            HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
            operations.putAll(key, fields);
        }
    }

    public void putFieldIfPresence(String key, String fieldKey, String fieldValue) {
        stringRedisTemplate.execute(hsetxxScript, singletonList(key), fieldKey, fieldValue);
    }

    public Map<String, String> getEntries(String key) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        return operations.entries(key);
    }

    public void deleteField(String key, String fieldKey) {
        HashOperations<String, String, String> operation = stringRedisTemplate.opsForHash();
        operation.delete(key, fieldKey);
    }

    public String getFieldValue(String key, String fieldKey) {
        HashOperations<String, String, String> operation = stringRedisTemplate.opsForHash();
        return operation.get(key, fieldKey);
    }

    public void deleteFieldInKeys(List<String> keys, String fieldKey) {
        byte[] byteFieldKey = fieldKey.getBytes();
        List<byte[]> byteFilteredDisConnectedKeys = keys.stream().map(String::getBytes).toList();

        int batchSize = 100;
        for (int start = 0; start < byteFilteredDisConnectedKeys.size(); start += batchSize) {
            int end = Math.min(start + batchSize, byteFilteredDisConnectedKeys.size());

            List<byte[]> tempByteKeys = byteFilteredDisConnectedKeys.subList(start, end);

            stringRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
                tempByteKeys.forEach(tempByteKey -> redisConnection.hashCommands().hDel(tempByteKey,
                        byteFieldKey));
                return null;
            });
        }
    }
}
