package com.fourback.bemajor.global.security;

import com.fourback.bemajor.global.exception.ExceptionEnum;
import com.fourback.bemajor.global.exception.kind.InvalidLoginTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
                throw new InvalidLoginTokenException(ExceptionEnum.INVALIDTOKEN.ordinal(),
                        "This is Invalid Token", HttpStatus.UNAUTHORIZED);
            }
            try {
                jwtUtil.isExpired(refresh);
            } catch (ExpiredJwtException e) {
                throw new InvalidLoginTokenException(ExceptionEnum.INVALIDTOKEN.ordinal(),
                        "This is Invalid Token", HttpStatus.UNAUTHORIZED);
            }

            String category = jwtUtil.getCategory(refresh);

            if (!category.equals("refresh")) {

                throw new InvalidLoginTokenException(ExceptionEnum.INVALIDTOKEN.ordinal(),
                        "This is Invalid Token", HttpStatus.UNAUTHORIZED);
            }

            Long userId = jwtUtil.getUserId(refresh);
            String role = jwtUtil.getRole(refresh);

            HttpHeaders httpHeaders = jwtUtil.createTokens(userId, role);
            String accessTokenName = "access";
            String refreshTokenName = "refresh";
            response.setHeader(accessTokenName, httpHeaders.getFirst(accessTokenName));
            response.setHeader(refreshTokenName, httpHeaders.getFirst(refreshTokenName));
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}