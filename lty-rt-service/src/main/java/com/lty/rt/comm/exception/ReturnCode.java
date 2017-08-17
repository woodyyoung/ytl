package com.lty.rt.comm.exception;

public enum ReturnCode {
	
	ERROR_05(-5,"插入数据失败"),
	ERROR_04(-4,"数据库数据错误"),
	ERROR_03(-3,"缺少参数"),
	ERROR_02(-2,"参数不正确"),
	ERROR_01(-1,"操作失败"),
	SUCCESS(0,"成功");
	
	private int code;
	
	private String msg;
	
	ReturnCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	

}
