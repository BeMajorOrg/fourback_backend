package com.fourback.bemajor.global.common.service;

import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
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
    private final Map<Long, Set<WebSocketSession>> sessionsByStudyGroupId;
    private final RedisTemplate<String, Long> stringLongRedisTemplate;

    public void setValueWithExpiredTime(RedisKeyPrefixEnum prefixEnum, Long id,
                                        String value, long expiredTime) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getDescription() + id,
                value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void setValue(RedisKeyPrefixEnum prefixEnum,
                         Long id, String value) {
        stringRedisTemplate.opsForValue().set(prefixEnum.getDescription() + id, value);
    }

    public void deleteKey(RedisKeyPrefixEnum prefixEnum, Long id) {
        stringRedisTemplate.delete(prefixEnum.getDescription() + id);
    }

    public String getValue(RedisKeyPrefixEnum prefixEnum, Long id) {
        return stringRedisTemplate.opsForValue().get(prefixEnum.getDescription() + id);
    }

    public boolean checkKey(RedisKeyPrefixEnum prefixEnum, Long id) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(
                prefixEnum.getDescription() + id));
    }

    public void putLongBooleanField(RedisKeyPrefixEnum prefixEnum,
                                    Long keyId, Long valueId, Boolean isAlarmSet) {
        HashOperations<String, Long, Boolean> operations = stringLongRedisTemplate.opsForHash();
        operations.put(prefixEnum.getDescription() + keyId, valueId, isAlarmSet);
    }

    public void putLongBooleanFields(RedisKeyPrefixEnum prefixEnum,
                                     Long keyId, Map<Long, Boolean> values) {
        if(values !=null && values.isEmpty()) {
            HashOperations<String, Long, Boolean> operations = stringLongRedisTemplate.opsForHash();
            operations.putAll(prefixEnum.getDescription() + keyId, values);
        }
    }

    public Map<Long, Boolean> EntriesLongBoolean(RedisKeyPrefixEnum prefixEnum, Long id) {
        HashOperations<String, Long, Boolean> operations = stringLongRedisTemplate.opsForHash();
        return operations.entries(prefixEnum.getDescription() + id);
    }

    public void deleteLongBooleanField(RedisKeyPrefixEnum prefixEnum, Long keyId, Long valueId) {
        HashOperations<String, Long, Boolean> operation = stringLongRedisTemplate.opsForHash();
        operation.delete(prefixEnum.getDescription() + keyId, valueId);
    }

//    public void removeUserInGroupSession(List<Long> keyIds, Long valueId) {
//        stringRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
//            byte[] valueByte = ByteBuffer.allocate(Long.BYTES)
//                    .putLong(valueId).array();
//            keyIds.forEach(keyId -> {
//                if (!sessionsByStudyGroupId.get(keyId).isEmpty()) {
//                    byte[] keyByte = (RedisKeyPrefixEnum.DISCONNECTED.getDescription() + keyId)
//                            .getBytes();
//                    redisConnection.setCommands().sRem(keyByte, valueByte);
//                }
//            });
//            return null;
//        });
//    }

//    @Scheduled(fixedDelay = 300000)
//    public void removeDisConnectedKey() {
//        Set<String> keys = stringRedisTemplate.keys(
//                RedisKeyPrefixEnum.DISCONNECTED + "*");
//        if (keys != null) {
//            List<byte[]> byteKeys = keys.stream().filter(key -> {
//                Long baseKey = Long.valueOf(key.split(":")[1]);
//                return sessionsByStudyGroupId.get(baseKey).isEmpty();
//            }).map(String::getBytes).toList();
//            int batchSize = 100;
//            int keySize = byteKeys.size();
//            for (int i = 0; i < (keySize + batchSize - 1) / batchSize; i++) {
//                List<byte[]> tempByteKeys = byteKeys.subList(
//                        i, i += i + batchSize < keySize ? batchSize : keySize % batchSize);
//                stringRedisTemplate.executePipelined(
//                        (RedisCallback<?>) redisConnection -> {
//                            tempByteKeys.forEach(key -> redisConnection.keyCommands().del(key));
//                            return null;
//                        });
//            }
//        }
//    }
}
