package com.lty.video.websocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lty.video.cache.DeviceCache;
import com.lty.video.cache.DeviceRes;
import com.lty.video.client.TcpClient;
import com.lty.video.handlers.ClientHandler;
import com.lty.video.handlers.TcpMessage;

public class SpringWebSocketHandler extends TextWebSocketHandler {
	public static final String WEBSOCKET_USERNAME = "WEBSOCKET_USERNAME";
	public  static final Map<String,WebSocketSession> users;//这个会出现性能问题，最好用Map来存储，key用userid
	private static final Logger logger = LoggerFactory.getLogger("bizLog");
	private String heartResponse = "{\"msg_type\":"+TcpMessage.MSG_WEBSOCKET_HEARTBEAT_RESPONSE+"}";
	
    static {
        users = new  ConcurrentHashMap<String, WebSocketSession>();
    }
    
    public SpringWebSocketHandler() {
    	
    }

    /**
     * 连接成功时候，会触发页面上onopen方法	
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	logger.info("hand client Connection begin!********");
        String SESSIONID= (String) session.getAttributes().get(WEBSOCKET_USERNAME);
        logger.info("client sessionId:"+getSessionIfo(SESSIONID, session.getRemoteAddress())+" connection sucess！********");
        users.put(SESSIONID,session);
        logger.info("current total client size:------"+users.size());
        DeviceRes res = new DeviceRes();
        res.setMsg_type(TcpMessage.MSG_GETONLINELIST_RESP);
        res.setResult(DeviceCache.getDevices());
        String deviceInfo = JSON.toJSONString(res);
        logger.info("push device msg to client!------");
        logger.info("push device num:"+(res.getResult()==null?0:res.getResult().size()));
        logger.debug(deviceInfo);
        TextMessage returnMessage =  new TextMessage(deviceInfo);
        session.sendMessage(returnMessage);
       	logger.info("hand client Connection end!********");
    }
    
    /**
     * 关闭连接时触发
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String SESSIONID= (String) session.getAttributes().get(WEBSOCKET_USERNAME);
        logger.info(getSessionIfo(SESSIONID, session.getRemoteAddress())+" connection closed！********");
        users.remove(SESSIONID);
        logger.info("current client total size:------"+users.size());
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override    
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	String sessionID= (String) session.getAttributes().get(WEBSOCKET_USERNAME);
    	users.put(sessionID,session);
    	logger.info("hand client msg! sessioninfo:----"+getSessionIfo(sessionID, session.getRemoteAddress()));
    	String content = message.getPayload();
    	logger.info("client content:----"+content);
    	if(content==null||content==""){
    		return;
    	}
		JSONObject parseObject = JSON.parseObject(content);
		int msg_type = parseObject.getIntValue("msg_type");
		//如果是心跳包
		if(msg_type==TcpMessage.MSG_WEBSOCKET_HEARTBEAT_REQUEST){
			handHeartBeat(sessionID, session);
			return;
		}
		String params = parseObject.getJSONObject("params").toJSONString();
		TcpMessage msg = TcpMessage.build(msg_type, params, sessionID);
		//获取NettyClient;
		TcpClient tcpClient = ClientHandler.getTcpClient();
		tcpClient.send(msg);
		logger.info("send msg to C++ end:----");
    }

    //处理心跳
    public void handHeartBeat(String sessionID,WebSocketSession session) throws IOException{
    	TextMessage msg = new TextMessage(heartResponse);
    	session.sendMessage(msg);
    }
    
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    	String sessionID= (String)session.getAttributes().get(WEBSOCKET_USERNAME);
    	logger.error("hand client msg error****!"+getSessionIfo(sessionID, session.getRemoteAddress())+""+exception.getMessage(),exception);
    	if(session.isOpen()){
        	session.close();
        }
        users.remove(sessionID);
    }

    public boolean supportsPartialMessages() {
        return false;
    }
    
    
    /**
     * 给某个用户发送消息
     *
     * @param sessionId
     * @param message
     * @throws IOException 
     */
    public void sendMessageToUser(String sessionId, TextMessage message) {
    	WebSocketSession session = users.get(sessionId);
    	if(session==null){
    		users.remove(sessionId);
    		return; 
    	}
    	if (session.isOpen()) {
    		 try {
				session.sendMessage(message);
			} catch (Exception e) {
				logger.error("send msg to client error!---"+getSessionIfo(sessionId, session.getRemoteAddress()),e);
				try {
					session.close();
				} catch (Exception e1) {
					logger.error("send msg to client error and close client error！"+getSessionIfo(sessionId, session.getRemoteAddress()),e1);
				}
			}
        }else{
        	users.remove(sessionId);
        }
    }
    
    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
    	for (Map.Entry<String,WebSocketSession> user: users.entrySet()) {
            try {
                if (user.getValue().isOpen()) {
                	user.getValue().sendMessage(message);
                }else{
                	users.remove(user.getKey());
                }
            } catch (Exception e) {
            	String sessionID= (String)user.getValue().getAttributes().get(WEBSOCKET_USERNAME);
            	logger.error("send msg to client error!--"+getSessionIfo(sessionID, user.getValue().getRemoteAddress()) ,e);
            	try {
					user.getValue().close();
				} catch (Exception e1) {
					logger.error("send msg to client error and close client error！--"+getSessionIfo(sessionID, user.getValue().getRemoteAddress()),e1);
				}
            }
        }
    }
    
    private String getSessionIfo(String sessionId,InetSocketAddress ip){
    	return String.format("ip:%s-%s", ip.toString(),sessionId); 
	}
}
