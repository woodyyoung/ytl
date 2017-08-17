package com.lty.video.cache;

import java.io.Serializable;
import java.util.List;

/**
 * 推送给web前端 的设备消息体
 * @author Administrator
 *
 */
public class DeviceRes implements Serializable {
	private static final long serialVersionUID = 1L;
	private int status = 1;
	private String message ="ok";
	private Integer msg_type;
	private List<Device> result;//设备
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(Integer msg_type) {
		this.msg_type = msg_type;
	}
	public List<Device> getResult() {
		return result;
	}
	public void setResult(List<Device> result) {
		this.result = result;
	}

}
