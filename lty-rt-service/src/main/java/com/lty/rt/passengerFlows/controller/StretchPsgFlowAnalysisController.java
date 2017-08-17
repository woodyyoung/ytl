package com.lty.rt.passengerFlows.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.basicData.bean.Stretch;
import com.lty.rt.basicData.service.StretchService;
import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.districtManagement.controller.AreaMapController;
import com.lty.rt.passengerFlows.service.StretchPsgFlowAnalysisService;

/**
 * 路段客流分析统计 Controller
 * 
 * @author Administrator
 * 
 */
@RequestMapping("/stretchAnalysis")
@Controller
public class StretchPsgFlowAnalysisController {

	private static final Logger logger = LoggerFactory.getLogger(AreaMapController.class);

	@Autowired
	private StretchPsgFlowAnalysisService stretchPsgFlowAnalysisService;
	@Autowired
	private StretchService stretchService;

	@RequestMapping("/xlfkfxForHours")
	@ResponseBody
	public RTResponse xlfkfxForHours(String areaCode, String beginDate, String endDate, String holidayFlag) {
		RTResponse res = new RTResponse();
		res.setData(stretchPsgFlowAnalysisService.queryStationFlowForHours(areaCode, beginDate, endDate, holidayFlag));
		return res;
	}

	@RequestMapping("/xlfkfxFordays")
	@ResponseBody
	public RTResponse xlfkfxFordays(String areaCode, String beginDate, String endDate, String beginDate2,
			String endDate2, String holidayFlag,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		res.setData(stretchPsgFlowAnalysisService.queryPsgFlowForDays(areaCode, beginDate, endDate, beginDate2,
				endDate2, holidayFlag,beginTime,endTime));
		return res;
	}

	@RequestMapping("/xlfkfxForWeeks")
	@ResponseBody
	public RTResponse xlfkfxForWeeks(String areaCode, String beginDate, String endDate, String beginDate2,
			String endDate2, String holidayFlag,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		res.setData(stretchPsgFlowAnalysisService.queryPsgFlowForWeeks(areaCode, beginDate, endDate, beginDate2,
				endDate2, holidayFlag,beginTime,endTime));
		return res;
	}

	@RequestMapping("/xlfkfxForMonths")
	@ResponseBody
	public RTResponse xlfkfxForMonths(String areaCode, String beginDate, String endDate, String beginDate2,
			String endDate2, String holidayFlag,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		res.setData(stretchPsgFlowAnalysisService.queryPsgFlowForMonths(areaCode, beginDate, endDate, beginDate2,
				endDate2, holidayFlag,beginTime,endTime));
		return res;
	}

	@RequestMapping("/listAllStretch")
	@ResponseBody
	public RTResponse listAllStretch(String stretchCode) {
		RTResponse res = new RTResponse();
		List<Stretch> list = stretchService.findListByMap(null);
		res.setData(list);
		return res;
	}

	@RequestMapping("/getAllSectionPsgFlowDataList")
	@ResponseBody
	public Map<String, Object> getAllSectionPsgFlowDataList(@RequestBody Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("stretchPsg", stretchPsgFlowAnalysisService.getAllSectionPsgFlowDataList(map));
		} catch (Exception e) {
			logger.error("stretchAnalysis.getAllSectionPsgFlowDataList() error{}", e);
		}
		return result;

	}

}
