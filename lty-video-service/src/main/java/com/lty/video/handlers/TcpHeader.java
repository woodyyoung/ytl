package com.lty.video.handlers;

public class TcpHeader {
	//#define MGPACKET_HEAD_FLAGS_0 ('L')
	//#define MGPACKET_HEAD_FLAGS_1 ('T')
	//#define MGPACKET_HEAD_FLAGS_2 ('Y')
	//#define MGPACKET_HEAD_FLAGS_3 ('0')
	private int uiMsgFlag;   //4个字节,消息标识(必须是 MGPACKET_HEAD_FLAGS 的网络字节序) 对应的值是811160652
	private int uiMsgType ;	 //消息类型
	private int uiMsgLen; 	 //消息体长度(不包含消息头长度) 
	private int uiVersion;   //4个字节,消息版本号 0x01
	private String uiSequence ;//消息序列号
	public TcpHeader(int uiMsgFlag, int uiMsgType, int uiMsgLen, int uiVersion, String uiSequance) {
		this.uiMsgFlag = uiMsgFlag;
        this.uiMsgType = uiMsgType;
        this.uiMsgLen = uiMsgLen;
        this.uiVersion = uiVersion;
        this.uiSequence = uiSequance;
    }

	public int getUiMsgType() {
		return uiMsgType;
	}
	public void setUiMsgType(int uiMsgType) {
		this.uiMsgType = uiMsgType;
	}
	public int getUiMsgLen() {
		return uiMsgLen;
	}
	public void setUiMsgLen(int uiMsgLen) {
		this.uiMsgLen = uiMsgLen;
	}
	public String getUiSequence() {
		return uiSequence;
	}
	public void setUiSequence(String uiSequance) {
		this.uiSequence = uiSequance;
	}

	public int getUiMsgFlag() {
		return uiMsgFlag;
	}

	public void setUiMsgFlag(int uiMsgFlag) {
		this.uiMsgFlag = uiMsgFlag;
	}

	public int getUiVersion() {
		return uiVersion;
	}

	public void setUiVersion(int uiVersion) {
		this.uiVersion = uiVersion;
	}
	
}
