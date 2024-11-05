package com.fourback.bemajor.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.script.DefaultRedisScript;


@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public DefaultRedisScript<Void> hsetxxScript() {
        String luaScript =
                "local key = KEYS[1] " +
                "local fieldKey = ARGV[1] " +
                "local fieldValue = ARGV[2] " +
                "if redis.call('EXISTS', key) == 1 then " +
                "    redis.call('HSET', key, fieldKey, fieldValue) " +
                "end";

        return new DefaultRedisScript<>(luaScript, Void.class);
    }
}
