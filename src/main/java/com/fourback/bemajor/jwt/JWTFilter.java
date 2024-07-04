package com.fourback.bemajor.jwt;

import com.fourback.bemajor.domain.CustomUserDetails;
import com.fourback.bemajor.dto.UserAuthDto;
import com.fourback.bemajor.exception.AccessTokenExpiredException;
import com.fourback.bemajor.exception.InvalidLoginTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("access");
        String path = request.getServletPath();
        String method = request.getMethod();
        if (accessToken == null) {
            if (isNotPostAuth(path, method)) {
                filterChain.doFilter(request, response);
            } else {
                log.debug("access token empty");
                throw new InvalidLoginTokenException(4, "This is Invalid Token. Try logging in again", HttpStatus.UNAUTHORIZED);
            }
            return;
        }
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            if(isNotPostAuth(path, method)) {
                throw new AccessTokenExpiredException(6, "Access Token Expired", HttpStatus.UNAUTHORIZED);
            }
            else{
                filterChain.doFilter(request, response);
            }
        }

        String category = jwtUtil.getCategory(accessToken);
        if(!category.equals("access")) {
            log.debug("invalid access token");
            throw new InvalidLoginTokenException(4, "This is Invalid Token. Try logging in again", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        UserAuthDto userAuthDto = new UserAuthDto(username, role);
        CustomUserDetails customOAuth2User = new CustomUserDetails(userAuthDto);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    private boolean isNotPostAuth(String path, String method){
        return !path.equals("/auth") || !method.equals("POST");
    }
}
