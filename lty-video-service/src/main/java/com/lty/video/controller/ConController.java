package com.lty.video.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.lty.video.handlers.TcpMessage;
@RequestMapping("/control")
@Controller
public class ConController extends BaseController {
	
	@RequestMapping("/exportlog")
	@ResponseBody
	public DeferredResult<String> exportlog(String bus_id) {
		if(bus_id==null){
			bus_id = "-1";
		}
		return this.call(TcpMessage.MSG_GETLOG_REQ,bus_id);
	}
	
}
