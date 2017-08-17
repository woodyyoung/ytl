package com.lty.rt.psgForecast.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.psgForecast.service.StretchPsgFlowForecastService;
import com.lty.rt.psgForecast.util.ForecastUtil;

/**
 * 路段断面客流分析统计 Controller
 * 
 * @author Administrator
 * 
 */
@RequestMapping("/stretchForecast")
@Controller
public class StretchPsgFlowForecastController {
	@Autowired
	private StretchPsgFlowForecastService stretchPsgFlowForecastService;
	
	@RequestMapping("/forecast")
	@ResponseBody
	public RTResponse forecast(@RequestBody Map<String, Object> map)
			throws ParseException {
		RTResponse res = null;
		try{
			
			String stretchId = (String) map.get("stretchId");
			if("-1".equals(stretchId)){
				map.put("stretchId", "");
			}
			
			map = ForecastUtil.validata(map);
			Map<String, Object> data = stretchPsgFlowForecastService.forecast(map);
			
			res = new RTResponse();
			res.setResCode(200);
			res.setData(data);
		
		}catch(Throwable ex){
			ex.printStackTrace();
			res = new RTResponse();
			res.setResCode(300);
			res.setMsg(ex.getMessage());
		}
		
		return res;
	}

/*	@RequestMapping("/forecast")
	@ResponseBody
	public RTResponse forecast(@RequestBody Map<String, Object> map)
			throws ParseException {
		RTResponse res = new RTResponse();
		String stretchId = (String) map.get("stretchId");
		if("-1".equals(stretchId)){
			map.put("stretchId", "");
		}
		String yc_month = (String) map.get("yc_month");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date firstDayOfMonth = sdf.parse(yc_month);
		Date endDayOfMonth = DateUtils.addDays(
				DateUtils.addMonths(firstDayOfMonth, 1), -1);
		sdf = new SimpleDateFormat("dd");
		int endDayIndex = Integer.parseInt(sdf.format(endDayOfMonth));
		String[] days = new String[endDayIndex];
		for (int i = 1; i <= endDayIndex; i++) {
			if (i < 10) {
				days[i - 1] = "0" + i;
			} else {
				days[i - 1] = "" + i;
			}
		}

		map.put("days", days);
		Map<String, Object> data = stretchPsgFlowForecastService.forecast(map);
		res.setData(data);
		return res;
	}*/

	@RequestMapping("/forecastOneDayData")
	@ResponseBody
	public RTResponse forecastOneDayData(@RequestBody Map<String, Object> map) {
		RTResponse res = new RTResponse();
		List<Map<String, Object>> data = stretchPsgFlowForecastService
				.forecastOneDayData(map);
		res.setData(data);
		return res;
	}
}
