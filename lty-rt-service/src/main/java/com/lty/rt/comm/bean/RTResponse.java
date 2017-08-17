package com.lty.rt.comm.bean;

public class RTResponse extends BaseBean {
	private static final long serialVersionUID = 1L;
	private int resCode = 200;
	private String msg = "success";
	private Object data;

	public int getResCode() {
		return resCode;
	}

	public void setResCode(int resCode) {
		this.resCode = resCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
