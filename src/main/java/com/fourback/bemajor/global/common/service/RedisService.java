package com.fourback.bemajor.global.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public void setRefreshToken(Long userId, String value,Long expiredTime){
        redisTemplate.opsForValue().set("Refresh:"+userId, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(Long userId){
        return redisTemplate.opsForValue().get("Refresh:"+userId);
    }

    public void deleteRefreshToken(Long userId){
        redisTemplate.delete("Refresh:"+userId);
    }
}
