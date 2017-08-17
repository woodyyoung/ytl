/*package com.lty.video.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSON;
import com.lty.video.cache.ChannelSpeed;
import com.lty.video.cache.Device;
import com.lty.video.cache.DeviceCache;
import com.lty.video.cache.DeviceNoStreamResponse;
import com.lty.video.handlers.TcpMessage;
import com.lty.video.websocket.SpringWebSocketHandler;

@Component
public class DeviceRateTask {
	@Autowired
	@Qualifier("deviceCache")
	private DeviceCache cache;//设备缓存
	
	@Value("${netty.channelLowerRateCount}")//通道低速次数
	private int channelLowerRateCount ;
	
	@Autowired
	@Qualifier("springWebSocketHandler")
	private SpringWebSocketHandler springWebSocketHandler;
	
	private static final Logger LOG = LoggerFactory.getLogger("bizLog");
	
	DeviceRateTask(){
		
	}
	@Scheduled(cron = "${netty.deviceTaskCron}")
    public void run(){ 
		 LOG.info("Device Rate Task begin!******");
		 List<Device> all = cache.getAll();
		 if(all.isEmpty()){
			 return;
		 }
		 DeviceNoStreamResponse 	response =new DeviceNoStreamResponse();
		 response.setMsg_type(TcpMessage.MSG_NOTIFY_STREAMSTATE);
		 List<DeviceNoStreamResponse.DeviceRate> list =new ArrayList<DeviceNoStreamResponse.DeviceRate>();
		 //遍历所有设备
		 for(Device device:all){
			 List<ChannelSpeed> stat = device.getStat();
			 if(stat==null||stat.isEmpty()){
				 continue;
			 }
			 //遍历所有设备通道速率
			 for(ChannelSpeed s:stat){
				 //如果低速率传输超过指定次数，此通道将推送设备流信息给前端
				 if(s.getLowerRateCount()>channelLowerRateCount){
					 LOG.info("busid:"+device.getBus_id()+" channelID:"+s.getChannel_id()+" has lower rate more than specify value！********");
					 DeviceNoStreamResponse.DeviceRate  rate =response.new DeviceRate();
					 rate.setBus_id(device.getBus_id());
					 rate.setRate(s.getSpeed());
					 rate.setChannel_id(s.getChannel_id());
					 s.setLowerRateCount(0);//重置为0
					 list.add(rate);
				 }
			 }
		 }
		 
		 if(list.isEmpty()){
			 LOG.info("no device channel rate lower acount more than:"+channelLowerRateCount);
			 return;
		 }
		 response.setResult(list);
		 LOG.info("lower rate channel num:"+list.size());
		 //发送超过指定次数 传输速率低于指定伐值的 通道
		 String returnJson = JSON.toJSONString(response);
		 TextMessage textMsg = new TextMessage(returnJson);
		 LOG.info("push lower rate msg to websocket begin！");
		 springWebSocketHandler.sendMessageToUsers(textMsg);
		 
		 LOG.info("Device Rate Task end!******");
    }  
	
}
*/