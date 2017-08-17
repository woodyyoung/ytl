package com.lty.video.handlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lty.video.cache.Device;
import com.lty.video.cache.DeviceCache;
import com.lty.video.cache.DeviceRes;
import com.lty.video.websocket.SpringWebSocketHandler;

@Component("deviceHandler")
public class DeviceHandler implements BaseHandler {
	private static final Logger LOG = LoggerFactory.getLogger("bizLog");
	@Autowired
	@Qualifier("deviceCache")
	private DeviceCache cahe;//设备缓存
	@Autowired
	@Qualifier("springWebSocketHandler")
	private SpringWebSocketHandler springWebSocketHandler;
	
	@Override
	public void hand(TcpMessage msg) {
		LOG.info("hand all device response begin！*******");
		String content = msg.getContent();
		int  msgType = msg.getTcpHeader().getUiMsgType();
		DeviceRes deviceRes = null;
		if(TcpMessage.MSG_GETONLINELIST_RESP == msgType){
			deviceRes = JSON.parseObject(content, DeviceRes.class);
			deviceRes.setMsg_type(msgType);
		}else{
			deviceRes = new DeviceRes();
			deviceRes.setMsg_type(msgType);
			Device device = JSON.parseObject(content, Device.class);
			List<Device> result = new ArrayList<Device>();
			result.add(device);
			deviceRes.setResult(result);
		}
		List<Device> result = deviceRes.getResult();
		if(result==null||result.isEmpty()){
			LOG.error("no device response!*************");
			return ;
		}
		//更新缓存
		LOG.info("response divice size:"+result.size());
		LOG.info("update cache begin！");
		//请求在线设备响应（全量）
        if(TcpMessage.MSG_GETONLINELIST_RESP == msgType){
        	//先清空缓存
    		cahe.cleanAll();
    		for(Device device:result){
    			device.setOnline(Device.STATUS_DEVICE_ONLINE);
    			cahe.update(device);
    		}
        }
        
        //推送设备状态信息（部分）
        if(TcpMessage.MSG_NOTIFY_DEVSTATE == msgType){
        	for(Device device:result){
        		if(device.getOnline().equals(Device.STATUS_DEVICE_NOT_ONLINE)){
        			cahe.remove(device);
        		}else{
         			cahe.update(device);
        		}
    		}
        }
		
		//websocket通知前端更新状态
		LOG.info("send msg to websocket begin!");
		String returnJson = JSON.toJSONString(deviceRes);
		TextMessage textMsg = new TextMessage(returnJson);
		springWebSocketHandler.sendMessageToUsers(textMsg);
		LOG.info("send msg to websocket over！");
		LOG.info("hand all device response end！*******");
		
		
	}

}
