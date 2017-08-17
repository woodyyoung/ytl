package com.lty.video.cache;

import java.io.Serializable;
import java.util.List;

public class DeviceNoStreamResponse implements Serializable{
	private static final long serialVersionUID = -6574203420782567243L;
	private Integer msg_type;
	private List<DeviceRate> result;
	
	public Integer getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(Integer msg_type) {
		this.msg_type = msg_type;
	}

	public List<DeviceRate> getResult() {
		return result;
	}

	public void setResult(List<DeviceRate> result) {
		this.result = result;
	}

	public class  DeviceRate implements Serializable{
		private static final long serialVersionUID = 1L;
		private Integer bus_id;
		private Integer channel_id;
		private Integer rate;
		private Integer state;
		public Integer getBus_id() {
			return bus_id;
		}
		public void setBus_id(Integer bus_id) {
			this.bus_id = bus_id;
		}
		public Integer getRate() {
			return rate;
		}
		public void setRate(Integer rate) {
			this.rate = rate;
		}
		public Integer getState() {
			return state;
		}
		public void setState(Integer state) {
			this.state = state;
		}
		public Integer getChannel_id() {
			return channel_id;
		}
		public void setChannel_id(Integer channel_id) {
			this.channel_id = channel_id;
		}
		
	}
	
	
	
}
