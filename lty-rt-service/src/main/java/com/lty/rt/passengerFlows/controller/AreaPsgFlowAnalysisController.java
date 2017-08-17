package com.lty.rt.passengerFlows.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.basicData.bean.PjmkArea;
import com.lty.rt.basicData.service.PjmkAreaService;
import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.passengerFlows.service.AreaPsgFlowAnalysisService;

/**
 * 小区客流分析统计 Controller
 * 
 * @author Administrator
 * 
 */
@RequestMapping("/areaAnalysis")
@Controller
public class AreaPsgFlowAnalysisController {
	@Autowired
	private AreaPsgFlowAnalysisService areaPsgFlowAnalysisService;
	@Autowired
	private PjmkAreaService pjmkAreaService;

	@RequestMapping("/xlfkfxForHours")
	@ResponseBody
	public RTResponse xlfkfxForHours(String areaCode, String beginDate, String endDate, String inlineRadioOptions, String targetAreaCode) {
		RTResponse res = new RTResponse();
		res.setData(areaPsgFlowAnalysisService.queryStationFlowForHours(areaCode, beginDate, endDate, inlineRadioOptions, targetAreaCode));
		return res;
	}

	@RequestMapping("/xlfkfxFordays")
	@ResponseBody
	public RTResponse xlfkfxFordays(String areaCode, String beginDate, String endDate, String beginDate2,
			String endDate2, String inlineRadioOptions, String targetAreaCode, String iswork,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		res.setData(areaPsgFlowAnalysisService.queryPsgFlowForDays(areaCode, beginDate, endDate, beginDate2, endDate2, inlineRadioOptions, targetAreaCode, iswork,beginTime,endTime));
		return res;
	}

	@RequestMapping("/xlfkfxForWeeks")
	@ResponseBody
	public RTResponse xlfkfxForWeeks(String areaCode, String beginDate, String endDate, String beginDate2,
			String endDate2, String inlineRadioOptions, String targetAreaCode, String iswork,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		res.setData(
				areaPsgFlowAnalysisService.queryPsgFlowForWeeks(areaCode, beginDate, endDate, beginDate2, endDate2, inlineRadioOptions, targetAreaCode, iswork,beginTime,endTime));
		return res;
	}

	@RequestMapping("/xlfkfxForMonths")
	@ResponseBody
	public RTResponse xlfkfxForMonths(String areaCode, String beginDate, String endDate, String beginDate2,
			String endDate2, String inlineRadioOptions, String targetAreaCode, String iswork,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		res.setData(
				areaPsgFlowAnalysisService.queryPsgFlowForMonths(areaCode, beginDate, endDate, beginDate2, endDate2, inlineRadioOptions, targetAreaCode, iswork,beginTime,endTime));
		return res;
	}

	@RequestMapping("/listAllArea")
	@ResponseBody
	public RTResponse listAllArea(String areaCode) {
		RTResponse res = new RTResponse();
		List<PjmkArea> list = pjmkAreaService.findListByMap(null);
		res.setData(list);
		return res;
	}

	// 查询源小区到目标小区的客流量
	@RequestMapping("/queryAreaPsgFlow")
	@ResponseBody
	public RTResponse queryAreaPsgFlow(@RequestBody Map<String, Object> map) {
		RTResponse res = new RTResponse();
		try {
			Map<String, Object> data = areaPsgFlowAnalysisService.queryAreaPsgFlow(map);// 查询小区进出客流量
			res.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
