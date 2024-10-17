package com.fourback.bemajor.global.config;

import com.fourback.bemajor.domain.chat.Interceptor.CustomHandshakeInterceptor;
import com.fourback.bemajor.domain.chat.handler.GroupChatHandler;
import com.fourback.bemajor.domain.friendchat.handler.FriendChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer {
    private final GroupChatHandler groupChatHandler;
    private final CustomHandshakeInterceptor customHandshakeInterceptor;
    private final FriendChatHandler friendChatHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(groupChatHandler, "/groupChat")
                .addInterceptors(customHandshakeInterceptor)
                .setAllowedOrigins("*");

        registry.addHandler(friendChatHandler, "/friendChat")
                .addInterceptors(customHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
