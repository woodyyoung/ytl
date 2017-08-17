package com.lty.rt.comm.response;

public class Response<T> {
	/** 错误或者成功代码 */
	private int code;
	/** 错误描述 */
	private String msg;
	/** 响应结果*/
	private T data;
	
	/**
	 * 构造函数
	 */
	public Response() {
	}
	/**
	 * 构造函数
	 * @param code    错误或者成功代码
	 * @param message 错误描述
	 */
	public Response(int code, String message) {
		this.code = code;
		this.msg = message;
	}
	
	/**
	 * 构造函数
	 * @param code    错误或者成功代码
	 * @param message 错误描述
	 * @param data    响应结果
	 * 
	 */
	public Response(int code, String message, T data) {
		this.code = code;
		this.msg = message;
		this.data = data;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}
}
