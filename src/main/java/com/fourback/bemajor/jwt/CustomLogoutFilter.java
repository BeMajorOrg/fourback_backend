package com.fourback.bemajor.jwt;

import com.fourback.bemajor.exception.InvalidLoginTokenException;
import com.fourback.bemajor.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, ServletException {

        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        String refresh = request.getHeader("refresh");

        //refresh null check
        if (refresh == null) {
            throw new InvalidLoginTokenException(4, "This is Invalid Token.", HttpStatus.UNAUTHORIZED);
        }
        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //response status code
            throw new InvalidLoginTokenException(4, "This is Invalid Token.", HttpStatus.UNAUTHORIZED);
        }
        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            //response status code
            throw new InvalidLoginTokenException(4, "This is Invalid Token.", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.getUsername(refresh);

        //DB에 저장되어 있는지 확인
        String refreshToken = redisService.getRefreshToken(username);
        if (refreshToken == null) {
            //response status code
            throw new InvalidLoginTokenException(4, "This is Invalid Token.", HttpStatus.UNAUTHORIZED);
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        redisService.deleteRefreshToken(username);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}