package com.lty.video.server;

import java.util.UUID;

import org.codehaus.jackson.map.util.JSONPObject;

import com.alibaba.fastjson.JSON;
import com.lty.video.cache.DeviceRes;
import com.lty.video.handlers.TcpMessage;

public class Test {
	public static final int MSG_GETONLINELIST_REQ = Integer.parseInt("100", 16);//0x100 请求在线列表256
	public static final int MSG_GETONLINELIST_RESP = Integer.parseInt("101", 16);//0x101请求在线列表应答 257
	public static final int MSG_GETLIVEURL_REQ = Integer.parseInt("102", 16);//0x102请求播放地址 258 
	public static final int MSG_GETLIVEURL_RESP = Integer.parseInt("103", 16);//0x103 请求播放地址应答259
	public static final int MSG_GETVODURL_REQ = Integer.parseInt("104", 16);//0x104 请求点播地址 260
	public static final int MSG_GETVODURL_RESP = Integer.parseInt("105", 16);//0x105 请求点播地址应答 261
	public static final int MSG_GETRECORD_REQ = Integer.parseInt("106", 16);//0x106查询录像262 
	public static final int MSG_GETRECORD_RESP = Integer.parseInt("107", 16);//0x107 查询录像应答263
	public static final int MSG_GETLOG_REQ = Integer.parseInt("108", 16);//0x108 通知设备上传日志264 
	public static final int MSG_GETLOG_RESP = Integer.parseInt("109", 16);//0x109通知设备上传日志应答265 
	
	public static final int  MSG_UPGRADE_REQ         = Integer.parseInt("10A", 16);//0x10A //通知设备升级266
	public static final int  MSG_UPGRADE_RESP        = Integer.parseInt("10B", 16);//0x10B //通知设备升级应答267
	public static final int  MSG_GET_BITRATE_REQ     = Integer.parseInt("10C", 16);//0x10C //获取视频码率268
	public static final int  MSG_GET_BITRATE_RESP    = Integer.parseInt("10D", 16);//0x10D //获取视频码率应答269
	public static final int  MSG_GET_MIRROR_REQ      = Integer.parseInt("10E", 16);//0x10E //获取视频镜像270
	public static final int  MSG_GET_MIRROR_RESQ     = Integer.parseInt("10F", 16);//0x10F //获取视频镜像应答271
	public static final int  MSG_GET_IMAGE_REQ       = Integer.parseInt("110", 16);//0x110 //获取图像参数272
	public static final int  MSG_GET_IMAGE_RESQ      = Integer.parseInt("111", 16);//0x111 //获取图像参数应答273
	public static final int  MSG_SET_BITRATE_REQ     = Integer.parseInt("112", 16);//0x112 //设置视频码率274
	public static final int  MSG_SET_BITRATE_RESP    = Integer.parseInt("113", 16);//0x113 //设置视频码率应答275
	public static final int  MSG_SET_MIRROR_REQ      = Integer.parseInt("114", 16);//0x114 //设置视频镜像276
	public static final int  MSG_SET_MIRROR_RESQ     = Integer.parseInt("115", 16);//0x115 //设置视频镜像应答277
	public static final int  MSG_SET_IMAGE_REQ       = Integer.parseInt("116", 16);//0x116 //设置图像参数278
	public static final int  MSG_SET_IMAGE_RESQ      = Integer.parseInt("117", 16);//0x117 //设置图像参数应答279	
	
	
	public static final int MSG_NOTIFY_DEVSTATE= Integer.parseInt("200", 16);//0x200 推送设备状态信息512
	public static final int MSG_NOTIFY_SERVERSTATE= Integer.parseInt("201", 16);//0x201推送服务器状态信息513 
	public static final int MSG_NOTIFY_STREAMSTATE= Integer.parseInt("202", 16);//0x202推送设备推流状态514
	public static final int MSG_HEARTBEAT = Integer.parseInt("203", 16);//0x203推送心跳包515
	
	
	public static void main(String[] args) {
		System.out.println(MSG_GETONLINELIST_REQ );
		System.out.println(MSG_GETONLINELIST_RESP );
		System.out.println(MSG_GETLIVEURL_REQ );
		System.out.println(MSG_GETLIVEURL_RESP );
		System.out.println(MSG_GETVODURL_REQ );
		System.out.println(MSG_GETVODURL_RESP );
		System.out.println(MSG_GETRECORD_REQ );
		System.out.println(MSG_GETRECORD_RESP );
		System.out.println(MSG_GETLOG_REQ );
		System.out.println(MSG_GETLOG_RESP );

		System.out.println("------------------------------");
		System.out.println( MSG_UPGRADE_REQ         );
		System.out.println( MSG_UPGRADE_RESP        );
		System.out.println( MSG_GET_BITRATE_REQ     );
		System.out.println( MSG_GET_BITRATE_RESP    );
		System.out.println( MSG_GET_MIRROR_REQ      );
		System.out.println( MSG_GET_MIRROR_RESQ     );
		System.out.println( MSG_GET_IMAGE_REQ       );
		System.out.println( MSG_GET_IMAGE_RESQ      );
		System.out.println( MSG_SET_BITRATE_REQ     );
		System.out.println( MSG_SET_BITRATE_RESP    );
		System.out.println( MSG_SET_MIRROR_REQ      );
		System.out.println( MSG_SET_MIRROR_RESQ     );
		System.out.println( MSG_SET_IMAGE_REQ       );
		System.out.println( MSG_SET_IMAGE_RESQ      );


		System.out.println("------------------------------");
		System.out.println(MSG_NOTIFY_DEVSTATE);
		System.out.println(MSG_NOTIFY_SERVERSTATE);
		System.out.println(MSG_NOTIFY_STREAMSTATE);
		System.out.println(MSG_HEARTBEAT );
		//testJson2();
	}
	
	
	public static  void testJson2(){
		Object parse = JSON.parse("{\"status\":1,\"message\":\"ok\",\"params\":\"{\"bus_id\":-1}\",\"result\":[{\"bus_id\":44029,\"channels\":[{\"online\":0,\"channel_id\":0},{\"online\":0,\"channel_id\":1},{\"online\":0,\"channel_id\":2},{\"online\":0,\"channel_id\":3},{\"online\":0,\"channel_id\":4}]}]}");
		//System.out.println(Integer.parseInt("202", 16)");
	}
}
