package com.interventionManager.config;

import com.interventionManager.services.chat.ChatServiceImpl;
import com.interventionManager.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class webSocketConfig implements WebSocketConfigurer {
    private final static String CHAT_ENDPOINT = "/chat";
    private final static String NOTIFICATION_ENDPOINT = "/notifications";
    private final ChatServiceImpl chatServiceImpl;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getChatWebSocketHandler(),"/chat/*")
                .setAllowedOrigins("*")
                //.withSockJS();
                .addInterceptors(new HttpSessionHandshakeInterceptor());
        registry.addHandler(getNotificationWebSocketHandler(),  "/notifications/*")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
    @Bean
    public WebSocketHandler getChatWebSocketHandler() {
        return  new ChatWebSocketHandler(chatServiceImpl);
    }
    @Bean
    public NotificationWebSocketHandler getNotificationWebSocketHandler() {
        return new NotificationWebSocketHandler();
    }

}
