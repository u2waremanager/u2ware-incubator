package io.github.u2ware.spring.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfiguration implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebSocketMessage.WS_URL).setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(WebSocketMessage.WS_PUBLISH_URL);
        
        // Enables a simple in-memory broker
        // registry.enableSimpleBroker(CHAT_SUBSCRIBE_URL);

        // Enable STOMP MessageQueue broker
        registry.enableStompBrokerRelay(WebSocketMessage.WS_SUBSCRIBE_URL)
		        .setRelayHost("localhost")
		        .setRelayPort(61613)
		        .setClientLogin("guest")
		        .setClientPasscode("guest");
    }
}
