package com.fourback.bemajor.global.security.jwt;

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

    public ReissueTokenFilter(final JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Boolean reissue = (Boolean) request.getAttribute("reissue");
        if (reissue != null && reissue) {
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
            String role = jwtUtil.getRole(refresh);

            List<Pair<String, String>> pairs = jwtUtil.createTokens(userId, role);
            pairs.forEach(pair -> response.setHeader(pair.getLeft(), pair.getRight()));

            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        filterChain.doFilter(request, response);
    }
}