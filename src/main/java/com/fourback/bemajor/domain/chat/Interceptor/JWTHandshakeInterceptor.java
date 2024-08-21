package com.fourback.bemajor.domain.chat.Interceptor;

import com.fourback.bemajor.global.security.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JWTHandshakeInterceptor implements HandshakeInterceptor {
    private final JWTUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String accessToken = request.getHeaders().getFirst("access");
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        Long userId = jwtUtil.getUserId(accessToken);
        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
