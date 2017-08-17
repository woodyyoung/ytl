package com.lty.video.cache;
public class ChannelSpeed {
	private Integer channel_id;//通道编号
	private String rate;//通道上传视频速率 
	private Integer speed;
	private int lowerRateCount;
	public Integer getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(Integer channel_id) {
		this.channel_id = channel_id;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
		if(rate.endsWith("Bytes")){
			this.speed = 0;
		}else{
			this.speed  = Integer.parseInt(rate.replaceAll("[^0-9]", ""));
		}
	}
	public int getLowerRateCount() {
		return lowerRateCount;
	}
	public void setLowerRateCount(int lowerRateCount) {
		this.lowerRateCount = lowerRateCount;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	
}
