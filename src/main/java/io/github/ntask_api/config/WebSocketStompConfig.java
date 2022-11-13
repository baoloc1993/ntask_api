package io.github.ntask_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //route mess with /app to controller
        registry.setApplicationDestinationPrefixes("/app");
        //route mess with /topic to broker
        registry.enableSimpleBroker("/topic");
//        registry.enableStompBrokerRelay("/topic").setRelayHost("localhost")
//            .setRelayPort(61613).setClientLogin("guest").setClientPasscode("guest");
    }

}
