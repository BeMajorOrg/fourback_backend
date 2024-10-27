package com.fourback.bemajor.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.global.exception.CustomException;
import com.fourback.bemajor.global.exception.ExceptionDto;
import com.fourback.bemajor.global.exception.kind.InvalidTokenException;
import com.fourback.bemajor.global.exception.kind.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    public JWTExceptionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException | InvalidTokenException exception) {
            setExceptionResponse(response, exception);
        }
    }

    private void setExceptionResponse(HttpServletResponse response, CustomException exception) throws IOException {
        response.setStatus(exception.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ExceptionDto exceptionDto = ExceptionDto.of(exception.getCode(), exception.getMessage());

        String jsonResponse = objectMapper.writeValueAsString(exceptionDto);

        response.getWriter().write(jsonResponse);
    }
}
