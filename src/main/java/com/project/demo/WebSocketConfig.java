package com.project.demo;

import com.project.demo.websocket.VideoControlHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer  {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(myWebSocketHandler(), "/ws")
                .setAllowedOrigins("http://localhost:4200")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

    @Bean
    public VideoControlHandler myWebSocketHandler() {
        return new VideoControlHandler();
    }

}
