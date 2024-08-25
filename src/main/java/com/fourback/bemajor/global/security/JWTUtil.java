package com.fourback.bemajor.global.security;

import com.fourback.bemajor.global.common.service.RedisService;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JWTUtil {
    private final SecretKey secretKey;
    private final RedisService redisService;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, RedisService redisService) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.redisService = redisService;
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("userId", Long.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String createToken(String category, Long userId, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category).claim("userId", userId)
                .claim("role", role).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs)).signWith(secretKey)
                .compact();
    }

    public List<Pair<String, String>> createTokens(Long userId, String role) {
        String access = "access";
        String refresh = "refresh";
        List<Pair<String, String>> tokens = new ArrayList<>(2);
        tokens.add(Pair.of(access, this.createToken(access, userId, role, 600000L)));
        String refreshToken = this.createToken(refresh, userId, role, 86400000L);
        tokens.add(Pair.of(refresh, refreshToken));
        redisService.setRefreshToken(userId, refreshToken, 86400000L);
        return tokens;
    }
}
