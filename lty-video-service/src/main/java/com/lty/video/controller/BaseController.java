package com.lty.video.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.lty.video.client.TcpClient;
import com.lty.video.handlers.ClientHandler;
import com.lty.video.handlers.TcpMessage;
@Component
public class BaseController {
	@Value("${spring.timeout}")  
	private  String timeout; 

	@Autowired
	@Qualifier("tcpClient")
	private TcpClient tcpClient;
	
	@Autowired
	@Qualifier("clientHandler")
	private ClientHandler clientHandler;
	
	public DeferredResult<String> call(int type,String busID){
		long overTime = Integer.parseInt(timeout);
		DeferredResult<String> result = new DeferredResult<String>(overTime);
		TcpMessage msg = TcpMessage.build(type,busID);
		final String sequenceID = msg.getTcpHeader().getUiSequence();//序列号
		clientHandler.suspendedTradeRequests.put(sequenceID, result);
		tcpClient.send(msg);
		result.onCompletion(new Runnable() {
			@Override
			public void run() {
				clientHandler.suspendedTradeRequests.remove(sequenceID);
			}
		});
		result.onTimeout(new Runnable() {
			@Override
			public void run() {
				clientHandler.suspendedTradeRequests.remove(sequenceID);
			}
		});
		return result;
	}
}
