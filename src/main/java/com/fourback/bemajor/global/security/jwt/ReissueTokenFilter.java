package com.fourback.bemajor.global.security.jwt;

import com.fourback.bemajor.global.common.enums.ExpiredTimeEnum;
import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.exception.kind.InvalidTokenException;
import com.fourback.bemajor.global.exception.kind.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ReissueTokenFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    public ReissueTokenFilter(final JWTUtil jwtUtil, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        String method = request.getMethod();
        if (path.equals("/reissue") && method.equals("POST")) {
            String refresh = request.getHeader("refresh");
            if (refresh == null) {
                throw new InvalidTokenException("Refresh Token is empty");
            }

            try {
                jwtUtil.isExpired(refresh);
            } catch (ExpiredJwtException e) {
                throw new TokenExpiredException("Refresh Token is expired");
            }

            String category = jwtUtil.getCategory(refresh);
            if (!category.equals("refresh")) {
                throw new InvalidTokenException("Unmatched Refresh Token Category");
            }

            Long userId = jwtUtil.getUserId(refresh);

            String refreshInRedis = redisService.getValue(RedisKeyPrefixEnum.REFRESH, userId);

            if(!refresh.equals(refreshInRedis)) {
                throw new InvalidTokenException("Unmatched Refresh Token in Redis");
            }

            String role = jwtUtil.getRole(refresh);

            List<Pair<String, String>> pairs = jwtUtil.createTokens(userId, role);
            pairs.forEach(pair -> response.setHeader(pair.getLeft(), pair.getRight()));

            redisService.extendExpiration(RedisKeyPrefixEnum.FCM, userId, ExpiredTimeEnum.FCM);

            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        filterChain.doFilter(request, response);
    }
}