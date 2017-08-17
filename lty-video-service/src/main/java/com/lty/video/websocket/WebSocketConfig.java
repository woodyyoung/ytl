package com.lty.video.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler(),"/websocket/socketServer.ws").addInterceptors(new SpringWebSocketHandlerInterceptor()).setAllowedOrigins("*");
        registry.addHandler(webSocketHandler(), "/sockjs/socketServer.ws").addInterceptors(new SpringWebSocketHandlerInterceptor()).setAllowedOrigins("*").withSockJS();
	}
	
	@Bean
    public TextWebSocketHandler webSocketHandler(){
        return springWebSocketHandler();
    }
	
	@Bean(name = "springWebSocketHandler")
	public SpringWebSocketHandler springWebSocketHandler(){
		return new SpringWebSocketHandler();
	}

}
