package com.fourback.bemajor.global.security.jwt;

import com.fourback.bemajor.global.common.enums.ExpiredTimeEnum;
import com.fourback.bemajor.global.common.enums.FieldKeyEnum;
import com.fourback.bemajor.global.common.enums.KeyPrefixEnum;
import com.fourback.bemajor.global.common.service.RedisService;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {
    private final SecretKey secretKey;
    private final RedisService redisService;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, RedisService redisService) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
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

    public Long getExpirationInMillis(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload().getExpiration().getTime();
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public List<Pair<String, String>> createTokens(Long userId, String role) {
        String access = "access";
        String refresh = "refresh";

        List<Pair<String, String>> tokens = new ArrayList<>(2);

        tokens.add(Pair.of(access, this.createToken(
                access, userId, role, ExpiredTimeEnum.ACCESS)));

        String refreshToken = this.createToken(refresh, userId, role, ExpiredTimeEnum.REFRESH);

        tokens.add(Pair.of(refresh, refreshToken));

        redisService.putFieldWithExpiredTime(KeyPrefixEnum.TOKENS.getKeyPrefix() + userId,
            FieldKeyEnum.REFRESH.getFieldKey(), refreshToken, ExpiredTimeEnum.REFRESH.getExpiredTime());

        return tokens;
    }

    private String createToken(String category, Long userId, String role, ExpiredTimeEnum expiredTimeEnum) {
        return Jwts.builder()
                .claim("category", category).claim("userId", userId)
                .claim("role", role).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTimeEnum.getExpiredTime()))
                .signWith(secretKey)
                .compact();
    }
}
