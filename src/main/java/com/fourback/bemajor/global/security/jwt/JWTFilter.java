package com.fourback.bemajor.global.security.jwt;

import com.fourback.bemajor.global.common.enums.FieldKeyEnum;
import com.fourback.bemajor.global.common.enums.KeyPrefixEnum;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.exception.kind.InvalidTokenException;
import com.fourback.bemajor.global.exception.kind.TokenExpiredException;
import com.fourback.bemajor.global.security.custom.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    public JWTFilter(JWTUtil jwtUtil, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("access");
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Access token expired");
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            throw new InvalidTokenException("Unmatched Access Token Category");
        }

        Long userId = jwtUtil.getUserId(accessToken);

        String logoutAccessToken = redisService.getValue(KeyPrefixEnum.LOGOUT_ACCESS.getKeyPrefix() + userId);
        String refreshToken = redisService.getFieldValue(
            KeyPrefixEnum.TOKENS.getKeyPrefix() + userId, FieldKeyEnum.REFRESH.getFieldKey());

        if (accessToken.equals(logoutAccessToken) || refreshToken == null) {
            throw new InvalidTokenException("Logout Access Token");
        }

        String role = jwtUtil.getRole(accessToken);

        CustomUserDetails userDetails = new CustomUserDetails(userId, role);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}