package com.lty.video.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;
/**
 * 设备缓存
 * @author Administrator
 *
 */
@Component("deviceCache")
public class DeviceCache {
	
	private  static Map<Integer, Device> cache = new ConcurrentHashMap<Integer, Device>();

	@PreDestroy
	public void destory(){
		cache.clear();
	}
	
	public void update(Device device){
		cache.put(device.getBus_id(), device);
	}
	
	public void remove(Device device){
		cache.remove(device.getBus_id());
	}
	
	public Device get(Integer busId){
		return cache.get(busId);
	}
	
	public  List<Device> getAll(){
		return new ArrayList<Device>(cache.values());
	}
	
	public static  List<Device> getDevices(){
		return new ArrayList<Device>(cache.values());
	}
	
	public  void cleanAll(){
		cache.clear();
	}
	
}
