package com.lty.rt.comm.exception;

public interface AppCode {

	String toString();
	String getMessage();
	void setMessage(String message);
	int getCode();
	void setCode(int code);


}
