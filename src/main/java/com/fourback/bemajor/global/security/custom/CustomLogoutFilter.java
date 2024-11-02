package com.fourback.bemajor.global.security.custom;

import com.fourback.bemajor.global.common.enums.KeyPrefixEnum;
import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.security.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final RedisService redisService;
    private final JWTUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response,
                          FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        if (!requestUri.matches("/logout") || !requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader("access");

        Long userId = jwtUtil.getUserId(accessToken);

        redisService.setValueWithExpiredTime(KeyPrefixEnum.LOGOUT_ACCESS.getKeyPrefix() + userId,
            accessToken, jwtUtil.getExpirationInMillis(accessToken));
        redisService.deleteKey(KeyPrefixEnum.TOKENS.getKeyPrefix() + userId);

        SecurityContextHolder.clearContext();

        response.setHeader("refresh", null);
        response.setHeader("access", null);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}