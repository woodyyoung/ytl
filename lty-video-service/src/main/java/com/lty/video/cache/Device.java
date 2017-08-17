package com.lty.video.cache;

import java.io.Serializable;
import java.util.List;

public class Device implements Serializable {
	public static final Integer STATUS_DEVICE_ONLINE = 1;
	public static final Integer STATUS_DEVICE_NOT_ONLINE = 0;
	private static final long serialVersionUID = 1L;
	private int bus_id ;//设备编号
	private String GPSR;//gps信号
	private Integer online ;//设备状态
	private List<DeviceChannels> channels;//通道状态
	//private List<ChannelSpeed> stat;//通道速率信息
	public Integer getOnline() {
		return online;
	}
	public void setOnline(Integer online) {
		this.online = online;
	}
	public int getBus_id() {
		return bus_id;
	}
	public void setBus_id(int bus_id) {
		this.bus_id = bus_id;
	}
	public String getGPSR() {
		return GPSR;
	}
	public void setGPSR(String gPSR) {
		GPSR = gPSR;
	}
	public List<DeviceChannels> getChannels() {
		return channels;
	}
	public void setChannels(List<DeviceChannels> channels) {
		this.channels = channels;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/*public List<ChannelSpeed> getStat() {
		return stat;
	}
	public void setStat(List<ChannelSpeed> stat) {
		this.stat = stat;
	}*/
	
	
	

}
