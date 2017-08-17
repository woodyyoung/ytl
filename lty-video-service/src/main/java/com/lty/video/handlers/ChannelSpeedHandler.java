/*package com.lty.video.handlers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lty.video.cache.ChannelSpeed;
import com.lty.video.cache.Device;
import com.lty.video.cache.DeviceCache;

@Component("ChannelSpeedHandler")
public class ChannelSpeedHandler implements BaseHandler {
	private static final Logger LOG = LoggerFactory.getLogger("bizLog");
	@Autowired
	@Qualifier("deviceCache")
	private DeviceCache cache;//设备缓存
	
	@Value("${netty.channelLowerRate}")//通道低速上传伐值
	private int channelLowerRate ;
	
	@Override
	public void hand(TcpMessage msg) {
		LOG.info("hand Channel Speed response begin！*******");
		String content = msg.getContent();
		Device  reciveDevice  = JSON.parseObject(content, Device.class);
		//获取缓存设备
		Device  cacheDevice =  cache.get(reciveDevice.getBus_id());
		if(cacheDevice==null){
			return ;
		}
		//如果之前设备没有速率信息
		List<ChannelSpeed> stat = cacheDevice.getStat();
		if(stat==null||stat.isEmpty()){
			cacheDevice.setStat(reciveDevice.getStat());
			return ;
		}
		
		//更新通道速率信息
		for(ChannelSpeed speed:stat){
			for(ChannelSpeed s:reciveDevice.getStat()){
				if(!s.getChannel_id().equals(speed.getChannel_id())){
					continue;
				}
				if(s.getSpeed()>channelLowerRate){
					speed.setLowerRateCount(0);
				}else{
					speed.setSpeed(s.getSpeed());
					speed.setLowerRateCount(speed.getLowerRateCount()+1);
				}
			}
		}
        
        LOG.info("hand Channel Speed response end！*******");
		
	}

}
*/