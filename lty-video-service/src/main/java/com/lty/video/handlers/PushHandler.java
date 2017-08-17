package com.lty.video.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lty.video.websocket.SpringWebSocketHandler;

@Component("pushHandler")
public class PushHandler implements BaseHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger("bizLog");
	
	@Autowired
	@Qualifier("springWebSocketHandler")
	private SpringWebSocketHandler springWebSocketHandler;

	@Override
	public void hand(TcpMessage msg) {
		LOG.info("pushHandler hand request response begin！*******");
		int msgType  = msg.getTcpHeader().getUiMsgType();
		String  sequence = msg.getTcpHeader().getUiSequence();
		LOG.info("sequenceID:"+sequence );
		JSONObject response = JSON.parseObject(msg.getContent());
		response.put("msg_type", msgType);
		
		
		
		//websocket响应前端
		LOG.info("push msg to websocket begin！");
		String returnJson = response.toJSONString();
		LOG.info("push to websocket msg:"+returnJson);
		TextMessage textMsg = new TextMessage(returnJson);
		
		springWebSocketHandler.sendMessageToUsers(textMsg);
		LOG.info("send msg to websocket over！");
		LOG.info("pushHandler request response end！*******");
		
	}

}
