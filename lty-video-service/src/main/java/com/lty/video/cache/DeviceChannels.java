package com.lty.video.cache;
import java.io.Serializable;
/**
 * 设备通道信息
 * @author Administrator
 *
 */
public class DeviceChannels implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer channel_id;//通道ID
	private Integer online;//在线状态
	public Integer getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(Integer channel_id) {
		this.channel_id = channel_id;
	}
	public Integer getOnline() {
		return online;
	}
	public void setOnline(Integer online) {
		this.online = online;
	}
	
}
