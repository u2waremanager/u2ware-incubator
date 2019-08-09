package io.github.u2ware.spring.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfiguration implements WebSocketMessageBrokerConfigurer {
	
    public static final String WS_CONNECTION    = "/connect";
    public static final String WS_BROADCASTING  = "/app/"; 
    public static final String WS_SUBSCRIPTIONS = "/topic/";
	
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WS_CONNECTION).setAllowedOrigins("*").withSockJS();
    }
    
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(10 * 1024);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(WS_BROADCASTING);
        
        // Enables a simple in-memory broker
        registry.enableSimpleBroker(WS_SUBSCRIPTIONS);

        // Enable STOMP MessageQueue broker
//        registry.enableStompBrokerRelay(WebSocketMessage.WS_SUBSCRIBE_URL)
//		        .setRelayHost("localhost")
//		        .setRelayPort(61613)
//		        .setClientLogin("guest")
//		        .setClientPasscode("guest");
    }
    
}
