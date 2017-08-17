package com.lty.rt.comm.exception;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/** 错误代码 */
	private int code;
	/**
	 * 构造函数
	 */
	public ApplicationException() {
	}
	/**
	 * 构造函数
	 * @param appCode	应用代码
	 */
	public ApplicationException(AppCode appCode) {
		super(appCode.getMessage());
		this.code = appCode.getCode();
	}

	/**
	 * 构造函数
	 * @param appCode	应用代码
	 * @param msgArgs   格式化消息参数，请参考{@linkplain java.lang.String String.format}
	 * 
	 */
	public ApplicationException(AppCode appCode, Object... msgArgs) {
		super(String.format(appCode.getMessage(), msgArgs));
		this.code = appCode.getCode();
	}
	
	/**
	 * 构造函数
	 * @param code		错误代码
	 * @param message	错误描述
	 */
	public ApplicationException(int code, String message) {
		super(message);
		this.code = code;
	}
	
	/**
	 * 构造函数
	 * @param code		错误代码
	 * @param message	错误描述
	 * @param cause		错误对象
	 */	
    public ApplicationException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
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

}
