package com.lty.video.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.lty.video.handlers.DeviceHandler;
import com.lty.video.handlers.TcpMessage;
import com.lty.video.websocket.SpringWebSocketHandler;
@RequestMapping("/video")
@Controller
public class VideoController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger("bizLog");
	
	@Autowired
	@Qualifier("springWebSocketHandler")
	private SpringWebSocketHandler springWebSocketHandler;
	@Autowired
	@Qualifier("deviceHandler")
	private  DeviceHandler deviceHandler;

	/*
	 * @RequestMapping("/add/{id}")
	 * 
	 * @ResponseBody public String add(@PathVariable String id){
	 * LOG.info("request parma:======="+id); System.out.println(id);
	 * serverHandler.sendMsg(id); return "{adfa:4}"; }
	 */
	
	@RequestMapping("/get_status")
	@ResponseBody
	public DeferredResult<String> getBusStatus(String bus_id) {
		deviceHandler.toString();
		if(bus_id==null){
			bus_id = "-1";
		}
		return this.call(TcpMessage.MSG_GETONLINELIST_REQ,bus_id);
	}
	
	@RequestMapping("/liveplay")
	@ResponseBody
	public DeferredResult<String> liveplay(String bus_id) {
		if(bus_id==null){
			bus_id = "-1";
		}
		return this.call(TcpMessage.MSG_GETLIVEURL_REQ,bus_id);
	}
	

}
