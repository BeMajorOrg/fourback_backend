package com.fourback.bemajor.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourback.bemajor.dto.ExceptionResponse;
import com.fourback.bemajor.exception.AccessTokenExpiredException;
import com.fourback.bemajor.exception.CustomException;
import com.fourback.bemajor.exception.InvalidLoginTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessTokenExpiredException | InvalidLoginTokenException exception) {
            setExceptionResponse(response, exception);
        }
    }

    private void setExceptionResponse(HttpServletResponse response, CustomException exception) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(exception.getStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getCode(), exception.getMessage());
        String jsonResponse = mapper.writeValueAsString(exceptionResponse);
        response.getWriter().write(jsonResponse);
    }
}
