package com.lty.video.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSON;
import com.lty.video.client.TcpClient;
import com.lty.video.handlers.TcpMessage;
import com.lty.video.websocket.SpringWebSocketHandler;

@Controller
public class WebsocketController {
	@Autowired
	@Qualifier("tcpClient")
	private TcpClient tcpClient ;
	@Bean//这个注解会从Spring容器拿出Bean
    public SpringWebSocketHandler infoHandler() {
        return new SpringWebSocketHandler();
    }
	@RequestMapping("/websocket/login")
	@ResponseBody
    public String login(String username,HttpServletRequest request) throws Exception {
        System.out.println(username+"登录");
        HttpSession session = request.getSession(false);
        session.setAttribute("SESSION_USERNAME", username);
        //response.sendRedirect("/quicksand/jsp/websocket.jsp");
        return "{sdfa}";
    }

    @RequestMapping("/websocket/send")
    @ResponseBody
    public String send( @RequestBody Map<String,Object> params  ,  HttpServletRequest request) {
    	String sessionID =  (String)params.get("sessionID");
     	String content =   JSON.toJSONString(params.get("content"));
        TextMessage msg = new TextMessage(content);
        if(sessionID==null||sessionID.trim().equals("-1")){
        	infoHandler().sendMessageToUsers(msg);
        }else{
        	infoHandler().sendMessageToUser(sessionID, msg);
        }
        return "has send to user!";
    }
    
    
    @RequestMapping("/netty/send")
    @ResponseBody
    public String nettySend(@RequestBody Map<String,Object> params  ,HttpServletRequest request) {
    	String sessionID =  (String)params.get("sessionID");
    	String content =  JSON.toJSONString(params.get("params"));
    	Integer msgType =   (Integer)params.get("msgType");
    	TcpMessage  msg = TcpMessage.build(msgType, content, sessionID);
    	tcpClient.send(msg);
    	return  "has send to c++!";
    }
    
    @RequestMapping("/netty/allDevice")
    @ResponseBody
    public String allDevice(HttpServletRequest request) {
    	System.out.println("request all device！");
    	tcpClient.requestAllDevice();
    	return  "has send to c++!";
    }
    


}
