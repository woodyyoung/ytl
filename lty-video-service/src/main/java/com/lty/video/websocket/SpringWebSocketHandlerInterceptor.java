package com.lty.video.websocket;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class SpringWebSocketHandlerInterceptor implements HandshakeInterceptor  {
		private static final Logger LOG = LoggerFactory.getLogger("bizLog");
		@Override
	    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
	            Map<String, Object> attributes) throws Exception {
			LOG.info("************Before Handshake************");
	        String sessionId = null;
	        if (request instanceof ServletServerHttpRequest) {
	            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
	            sessionId = servletRequest.getServletRequest().getParameter("sessionID");
	            LOG.info("************sessionId:***********"+sessionId);
	            LOG.info("************client address:***********"+request.getRemoteAddress());
	            if(sessionId == null||sessionId.trim().equals("")){
	            	LOG.info("hand shake error:-----------no sessionID");
	            	return false;
	            }
	            if(sessionId.length()!=32){
	            	LOG.info("hand shake error:-----------sessionID error!"+sessionId);
	            	return false;
	            }
	            attributes.put(SpringWebSocketHandler.WEBSOCKET_USERNAME,sessionId);
	            
	            WebSocketSession session = SpringWebSocketHandler.users.remove(sessionId);
	            if(session!=null){
	            	session.close();
	            }
	        }
	        LOG.info("hand shake suceess!:-----------sessionId:"+sessionId);
	        return true;
	    }
	    
		@Override
		public void afterHandshake(ServerHttpRequest request,
			ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
			  
		
		}
		
		  
}
