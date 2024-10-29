package com.fourback.bemajor.domain.studygroup.service;

import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupRedisService {
    private final StringRedisTemplate stringRedisTemplate;
    private final Map<Long, Set<WebSocketSession>> sessionsByStudyGroupId;

    public void deleteDisconnectedUserFromActiveChats(List<Long> keyIds, Long valueId) {
        byte[] byteValueId = valueId.toString().getBytes();
        List<byte[]> byteFilteredDisConnectedKeys = keyIds.stream()
                .filter(keyId -> !sessionsByStudyGroupId.get(keyId).isEmpty())
                .map(keyId -> (RedisKeyPrefixEnum.DISCONNECTED.getDescription() + keyId).getBytes())
                .toList();

        stringRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
            byteFilteredDisConnectedKeys.forEach(byteFilteredDisConnectedKey ->
                    redisConnection.hashCommands().hDel(byteFilteredDisConnectedKey, byteValueId));
            return null;
        });
    }

    @Scheduled(fixedDelay = 3000000)
    public void deleteDisConnectedUserFromInactiveChats() {
        Set<String> disConnectedKeys = stringRedisTemplate
                .keys(RedisKeyPrefixEnum.DISCONNECTED.getDescription() + "*");
        if (disConnectedKeys != null) {
            List<byte[]> byteFilteredDisConnectedKeys = disConnectedKeys.stream()
                    .filter(disConnectedKey -> {
                        Long disConnectedKeyId = Long.valueOf(disConnectedKey.split(":")[1]);
                        return sessionsByStudyGroupId.get(disConnectedKeyId).isEmpty();
                    })
                    .map(String::getBytes).toList();

            int batchSize = 100;
            for (int start = 0; start < byteFilteredDisConnectedKeys.size(); start += batchSize) {
                int end = Math.min(start + batchSize, byteFilteredDisConnectedKeys.size());

                List<byte[]> tempByteKeys = byteFilteredDisConnectedKeys.subList(start, end);

                stringRedisTemplate.executePipelined((RedisCallback<?>) redisConnection -> {
                    tempByteKeys.forEach(tempByteKey -> redisConnection.keyCommands().del(tempByteKey));
                    return null;
                });
            }
        }
    }
}
