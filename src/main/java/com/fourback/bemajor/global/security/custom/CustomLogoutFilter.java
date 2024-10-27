package com.fourback.bemajor.global.security.custom;

import com.fourback.bemajor.global.common.enums.RedisKeyPrefixEnum;
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
        if (isNotLogoutRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = request.getHeader("refresh");

        Long userId = jwtUtil.getUserId(refreshToken);

        redisService.deleteKey(RedisKeyPrefixEnum.REFRESH, userId);
        redisService.deleteKey(RedisKeyPrefixEnum.FCM, userId);

        SecurityContextHolder.clearContext();

        response.setHeader("refresh", null);
        response.setHeader("access", null);

        response.setStatus(HttpServletResponse.SC_OK);
    }

    private boolean isNotLogoutRequest(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();

        return !requestUri.matches("/logout") || !requestMethod.equals("POST");
    }
}