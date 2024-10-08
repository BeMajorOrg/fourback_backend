package com.fourback.bemajor.global.security;

import com.fourback.bemajor.global.exception.kind.TokenExpiredException;
import com.fourback.bemajor.global.exception.kind.InvalidTokenException;
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

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
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
            String path = request.getServletPath();
            String method = request.getMethod();
            if (path.equals("/reissue") && method.equals("POST")) {
                request.setAttribute("reissue", true);
                filterChain.doFilter(request, response);
            } else {
                throw new TokenExpiredException("Access token expired");
            }
            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            log.debug("invalid access token");
            throw new InvalidTokenException("Unmatched Access Token Category");
        }

        Long userId = jwtUtil.getUserId(accessToken);
        String role = jwtUtil.getRole(accessToken);
        CustomUserDetails customOAuth2User = new CustomUserDetails(userId, role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User,
                null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}