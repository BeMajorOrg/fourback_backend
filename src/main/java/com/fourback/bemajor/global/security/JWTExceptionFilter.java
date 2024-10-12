package com.fourback.bemajor.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.global.exception.ExceptionDto;
import com.fourback.bemajor.global.exception.kind.TokenExpiredException;
import com.fourback.bemajor.global.exception.CustomException;
import com.fourback.bemajor.global.exception.kind.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException | InvalidTokenException exception) {
            setExceptionResponse(response, exception);
        }
    }

    private void setExceptionResponse(HttpServletResponse response, CustomException exception)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(exception.getStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ExceptionDto exceptionDto = new ExceptionDto(exception.getCode(), exception.getMessage());
        String jsonResponse = mapper.writeValueAsString(exceptionDto);
        response.getWriter().write(jsonResponse);
    }
}
