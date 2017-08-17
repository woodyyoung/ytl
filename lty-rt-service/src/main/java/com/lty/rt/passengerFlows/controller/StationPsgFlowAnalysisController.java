package com.lty.rt.passengerFlows.controller;

import java.text.ParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.basicData.bean.Line;
import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.passengerFlows.service.StationPsgFlowAnalysisService;
import com.lty.rt.psgForecast.util.ForecastUtil;

/**
 * 站台客流分析统计 Controller
 * 
 * @author Administrator
 * 
 */
@RequestMapping("/template")
@Controller
public class StationPsgFlowAnalysisController {
	@Autowired
	private StationPsgFlowAnalysisService stationPsgFlowAnalysisService;

	@RequestMapping("/ztfkfx")
	@ResponseBody
	public RTResponse ztfkfx(String beginDate, String endDate,
			@RequestParam(value = "queryPlatforms[]") String[] queryPlatforms, String queryTPlatform, String psgType,
			String holidayFlag) {
		RTResponse res = new RTResponse();
		if ("1".equals(psgType)) {
			res.setData(stationPsgFlowAnalysisService.queryStationFlow(beginDate, endDate, queryPlatforms[0].split("=")[0],
					queryTPlatform.split("=")[0], holidayFlag));// 站台间客流
		} else {
			if (StringUtils.isBlank(endDate)) {
				endDate = null;
			}
			res.setData(
					stationPsgFlowAnalysisService.queryStationFlow(beginDate, endDate, queryPlatforms[0], holidayFlag));// 站台集散量
		}

		return res;
	}

	@RequestMapping("/ztfkfxFordays")
	@ResponseBody
	public RTResponse ztfkfxFordays(String beginDate, String endDate, String beginDate2, String endDate2,String beginTime,String endTime,
			@RequestParam(value = "queryPlatforms[]") String[] queryPlatforms, String queryTPlatform, String psgType,
			String holidayFlag) {
		RTResponse res = new RTResponse();
		if ("1".equals(psgType)) {	//站台集散量   //站台间客流
			res.setData(stationPsgFlowAnalysisService.queryPsgFlowForDays(beginDate, endDate, beginDate2, endDate2,beginTime,endTime,
					queryPlatforms[0].split("=")[0], queryTPlatform.split("=")[0], holidayFlag));// 站台间客流
		} else {
			res.setData(stationPsgFlowAnalysisService.queryPsgFlowForDays(beginDate, endDate, beginDate2, endDate2,beginTime,endTime,
					queryPlatforms[0], holidayFlag));
		}
		return res;
	}

	@RequestMapping("/ztfkfxForWeeks")
	@ResponseBody
	public RTResponse ztfkfxForWeeks(String beginDate, String endDate, String beginDate2, String endDate2,
			@RequestParam(value = "queryPlatforms[]") String[] queryPlatforms, String queryTPlatform, String psgType,
			String holidayFlag,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		if ("1".equals(psgType)) {
			res.setData(stationPsgFlowAnalysisService.queryPsgFlowForWeeks(beginDate, endDate, beginDate2, endDate2,
					queryPlatforms[0].split("=")[0], queryTPlatform.split("=")[0], holidayFlag,beginTime,endTime));// 站台间客流
		} else {
			res.setData(stationPsgFlowAnalysisService.queryPsgFlowForWeeks(beginDate, endDate, beginDate2, endDate2,
					queryPlatforms[0], holidayFlag,beginTime,endTime));
		}
		return res;
	}

	@RequestMapping("/ztfkfxForMonths")
	@ResponseBody
	public RTResponse ztfkfxForMonths(String beginDate, String endDate, String beginDate2, String endDate2,
			@RequestParam(value = "queryPlatforms[]") String[] queryPlatforms, String queryTPlatform, String psgType,
			String holidayFlag,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		if ("1".equals(psgType)) {
			res.setData(stationPsgFlowAnalysisService.queryPsgFlowForMonths(beginDate, endDate, beginDate2, endDate2,
					queryPlatforms[0].split("=")[0], queryTPlatform.split("=")[0], holidayFlag,beginTime,endTime));// 站台间客流
		} else {
			res.setData(stationPsgFlowAnalysisService.queryPsgFlowForMonths(beginDate, endDate, beginDate2, endDate2,
					queryPlatforms[0], holidayFlag,beginTime,endTime));
		}
		return res;
	}

	@RequestMapping("/listPlatform")
	@ResponseBody
	public RTResponse listPlatform(Line line) {
		RTResponse res = new RTResponse();
		res.setData(stationPsgFlowAnalysisService.listPlatform());
		return res;
	}
}
