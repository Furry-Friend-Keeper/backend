package com.example.furryfriendkeeper;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketConfigCors extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}