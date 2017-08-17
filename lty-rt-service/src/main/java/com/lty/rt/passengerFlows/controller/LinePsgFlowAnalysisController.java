package com.lty.rt.passengerFlows.controller;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.assessment.controller.IndexController;
import com.lty.rt.basicData.bean.Line;
import com.lty.rt.basicData.service.LineService;
import com.lty.rt.basicData.service.StationService;
import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.comm.util.GpsUtils;
import com.lty.rt.passengerFlows.bean.PlatFormPsgFlow;
import com.lty.rt.passengerFlows.service.LinePsgFlowAnalysisService;

/**
 * 线路客流分析统计 Controller
 * 
 * @author Administrator
 * 
 */
@RequestMapping("/lineAnalysis")
@Controller
public class LinePsgFlowAnalysisController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private LinePsgFlowAnalysisService linePsgFlowAnalysisService;
	@Autowired
	private StationService stationService;
	@Autowired
	private LineService lineService;

	@RequestMapping("/xlfkfxForHours")
	@ResponseBody
	public RTResponse xlfkfxForHours(String lineId, String beginDate, String endDate, String stationId,
			String inlineRadioOptions, String targetStationId) {
		RTResponse res = new RTResponse();
		try {
			Map<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("lineId", lineId);
			conditionMap.put("beginDate", beginDate);
			conditionMap.put("endDate", endDate);
			conditionMap.put("stationId", stationId);
			conditionMap.put("inlineRadioOptions", inlineRadioOptions);
			conditionMap.put("targetStationId", targetStationId);
			res.setData(linePsgFlowAnalysisService.queryStationFlowForHours(conditionMap));
		} catch (Exception e) {
			logger.error("index.getLists() error{}", e);
		}
		return res;
	}

	@RequestMapping("/xlfkfxFordays")
	@ResponseBody
	public RTResponse xlfkfxFordays(String lineId, String beginDate, String endDate, String beginDate2, String endDate2,
			String stationId, String inlineRadioOptions, String targetStationId, String iswork,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		res.setData(linePsgFlowAnalysisService.queryPsgFlowForDays(lineId, beginDate, endDate, beginDate2, endDate2,
				stationId, inlineRadioOptions, targetStationId, iswork,beginTime,endTime));
		return res;
	}

	@RequestMapping("/xlfkfxForWeeks")
	@ResponseBody
	public RTResponse xlfkfxForWeeks(String lineId, String beginDate, String endDate, String beginDate2,
			String endDate2, String stationId, String inlineRadioOptions, String targetStationId, String iswork,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		res.setData(linePsgFlowAnalysisService.queryPsgFlowForWeeks(lineId, beginDate, endDate, beginDate2, endDate2,
				stationId, inlineRadioOptions, targetStationId, iswork,beginTime,endTime));
		return res;
	}

	@RequestMapping("/xlfkfxForMonths")
	@ResponseBody
	public RTResponse xlfkfxForMonths(String lineId, String beginDate, String endDate, String beginDate2,
			String endDate2, String stationId, String inlineRadioOptions, String targetStationId, String iswork,String beginTime,String endTime) {
		RTResponse res = new RTResponse();
		res.setData(linePsgFlowAnalysisService.queryPsgFlowForMonths(lineId, beginDate, endDate, beginDate2, endDate2,
				stationId, inlineRadioOptions, targetStationId, iswork,beginTime,endTime));
		return res;
	}

	@RequestMapping("/listAllLine")
	@ResponseBody
	public RTResponse listPlatform(Line line) {
		RTResponse res = new RTResponse();
		res.setData(linePsgFlowAnalysisService.listAllLine());
		return res;
	}

	@RequestMapping("/queryStation")
	@ResponseBody
	public RTResponse queryStation(String lineId) {
		RTResponse res = new RTResponse();
		res.setData(stationService.queryStationByLineId(lineId));
		return res;
	}

	@RequestMapping("/getLinePsgFlowDataList")
	@ResponseBody
	public Map<String, Object> getLinePsgFlowDataList(@RequestBody Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String dataType = map.get("dataType") == null ? "" : map.get("dataType").toString();
		String lineId = map.get("lineId") == null ? "" : map.get("lineId").toString();
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		String endDate = map.get("endTime") == null ? "" : map.get("endTime").toString();
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("lineId", lineId);
		conditionMap.put("startTime", beginDate);
		if(map.get("holidayFlag")!=null){
			conditionMap.put("holidayFlag", map.get("holidayFlag"));
		}
		if (StringUtils.isNotBlank(lineId)) {
			try {
				if ("H".equals(dataType)) {
					String aloneDay = map.get("aloneDay") == null ? "" : map.get("aloneDay").toString();
					String aloneHour = (String) map.get("aloneHour") == null ? "" : map.get("aloneHour").toString();
					conditionMap.put("aloneHour", aloneHour);
					conditionMap.put("aloneDay", aloneDay);
					conditionMap.put("dataType", "0");
					beginDate = aloneDay;
					result.put("linePsgData", linePsgFlowAnalysisService.queryPsgFlowForHourToHour(conditionMap));
				} else if ("D".equals(dataType)) {
					String aloneDay = map.get("aloneDay") == null ? "" : map.get("aloneDay").toString();
					conditionMap.put("aloneDay", aloneDay);
					conditionMap.put("dataType", "1");
					conditionMap.put("endTime", endDate);
					result.put("linePsgData", linePsgFlowAnalysisService.queryPsgFlowForDayToDay(conditionMap));
				} else if ("W".equals(dataType)) {
					String aloneWeek = map.get("aloneWeek") == null ? "" : map.get("aloneWeek").toString();
					conditionMap.put("aloneWeek", aloneWeek);
					conditionMap.put("dataType", "2");
					conditionMap.put("endTime", endDate);
					result.put("linePsgData", linePsgFlowAnalysisService.queryPsgFlowForWeekToWeek(conditionMap));
				} else if ("M".equals(dataType)) {
					String aloneMonth = (String) map.get("aloneMonth") == null ? "" : map.get("aloneMonth").toString();
					conditionMap.put("aloneMonth", aloneMonth);
					conditionMap.put("dataType", "3");
					conditionMap.put("endTime", endDate);
					result.put("linePsgData", linePsgFlowAnalysisService.queryPsgFlowForMonthToMonth(conditionMap));
				} else {
					result.put("linePsgData", null);
				}
				conditionMap.put("startTime", beginDate);
				conditionMap.put("endTime", endDate);
				List<Map<String, Object>> stationList = linePsgFlowAnalysisService
						.queryStationPsgFlowByLineId(conditionMap);// 查询站点坐标以及离散客流量
				Line oLine = lineService.selectByPrimaryKey(lineId);
				String lPath = ClobToString((Clob) oLine.getLpath());
				result.put("stationsAndLinePath", GpsUtils.findStationsAndCorrectLpath(lPath, stationList));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@RequestMapping("/getAllLinePsgFlowDataList")
	@ResponseBody
	public Map<String, Object> getAllLinePsgFlowDataList(@RequestBody Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String dataType = (String) map.get("dataType");
		String lineId = (String) map.get("lineId");
		String beginDate = (String) map.get("startTime");
		String endDate = (String) map.get("endTime");
		String holidayFlag = (String) map.get("holidayFlag");
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("lineId", lineId);
		conditionMap.put("startTime", beginDate);
		conditionMap.put("holidayFlag", holidayFlag);
		conditionMap.put("endTime", endDate);
		try {
			if ("H".equals(dataType)) {
				String aloneDay = (String) map.get("aloneDay");
				String aloneHour = (String) map.get("aloneHour");
				conditionMap.put("aloneHour", aloneHour);
				conditionMap.put("aloneDay", aloneDay);
				conditionMap.put("dataType", "0");
				result.put("linePsgData", linePsgFlowAnalysisService.queryAllPsgFlowForHourToHour(conditionMap));
			} else if ("D".equals(dataType)) {
				String aloneDay = (String) map.get("aloneDay");
				conditionMap.put("aloneDay", aloneDay);
				conditionMap.put("dataType", "1");
				result.put("linePsgData", linePsgFlowAnalysisService.queryAllPsgFlowForDayToDay(conditionMap));
			} else if ("W".equals(dataType)) {
				String aloneWeek = (String) map.get("aloneWeek");
				conditionMap.put("aloneWeek", aloneWeek);
				conditionMap.put("dataType", "2");
				result.put("linePsgData", linePsgFlowAnalysisService.queryAllPsgFlowForWeekToWeek(conditionMap));
			} else if ("M".equals(dataType)) {
				String aloneMonth = (String) map.get("aloneMonth");
				conditionMap.put("dataType", "3");
				conditionMap.put("aloneMonth", aloneMonth);
				result.put("linePsgData", linePsgFlowAnalysisService.queryAllPsgFlowForMonthToMonth(conditionMap));
			} else {
				result.put("linePsgData", null);
			}
			if (StringUtils.isNoneBlank(lineId)) {
				List<Map<String, Object>> stationList = linePsgFlowAnalysisService
						.queryPlatFormPsgFlowByLineId(conditionMap);// 查询站点坐标以及离散客流量
				Line oLine = lineService.selectByPrimaryKey(lineId);
				String lPath = ClobToString((Clob) oLine.getLpath());
				result.put("allStationsAndLinePath", GpsUtils.findStationsAndCorrectLpath(lPath, stationList));
			} else {
				List<Line> allLines = linePsgFlowAnalysisService.listAllLine();
				List<Map<String, Object>> allStationsAndLinePath = new ArrayList<Map<String, Object>>();
				for (Line l : allLines) {
					conditionMap.put("lineId", l.getId());
					List<Map<String, Object>> stationList = linePsgFlowAnalysisService
							.queryPlatFormPsgFlowByLineId(conditionMap);// 查询站点坐标以及离散客流量
					Line oLine = lineService.selectByPrimaryKey(l.getId());
					String lPath = ClobToString((Clob) oLine.getLpath());
					Map<String, Object> resultData = GpsUtils.findStationsAndCorrectLpath(lPath, stationList);
					List<List<String>> platFormList = (List<List<String>>) resultData.get("stationData");
					List<Map<String, Object>> newplatFormList = new ArrayList<Map<String, Object>>();// 用来接收放入线路客流信息属性的站台信息
					for (int i = 0; i < platFormList.size(); i++) {
						List<String> plats = platFormList.get(i);
						Map<String, Object> plat = new HashMap<String, Object>();
						String latitude = plats.get(0) == null ? "" : plats.get(0).toString();
						String longitude = plats.get(1) == null ? "" : plats.get(1).toString();
						String platFormName = plats.get(3) == null ? "" : plats.get(3).toString();
						String platFormId = plats.get(4) == null ? "" : plats.get(4).toString();
						conditionMap.put("platFormId", platFormId);
						List<Map<String, Object>> linesPsg = linePsgFlowAnalysisService
								.queryPlatFormPsgFlow(conditionMap);
						plat.put("longitude", longitude);// 经度
						plat.put("latitude", latitude);// 纬度
						plat.put("platFormName", platFormName);// 纬度
						plat.put("lines", linesPsg);// 线路信息集合
						newplatFormList.add(plat);
					}
					resultData.put("stationData", newplatFormList);
					allStationsAndLinePath.add(resultData);
				}
				conditionMap.put("lineId", "");
				result.put("allStationsAndLinePath", allStationsAndLinePath);
			}
			List<PlatFormPsgFlow> platFormList = linePsgFlowAnalysisService.getAllPlatFormData(conditionMap);// 查询出所有站台的客流
			if (CollectionUtils.isNotEmpty(platFormList)) {
				for (int i = 0; i < platFormList.size(); i++) {
					PlatFormPsgFlow ppf = platFormList.get(i);
					String platFormId = ppf.getPlatformId();
					conditionMap.put("platform_id", platFormId);
					List<Map<String, Object>> lines = linePsgFlowAnalysisService.getLinePsgByPlatFormId(conditionMap);
					ppf.setLines(lines);
				}
			}
			result.put("allPlats", platFormList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 将数据库CLOB字段转换成String类型
	private String ClobToString(Clob clob) {
		String reString = "";
		try {
			Reader is = clob.getCharacterStream();// 得到流
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
				sb.append(s);
				s = br.readLine();
			}
			reString = sb.toString();
		} catch (Exception e) {
		}
		return reString;
	}

	@RequestMapping("/getAllLinePsgTest")
	@ResponseBody
	public Map<String, Object> getAllLinePsgTest(@RequestBody Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, List<List<List<List<String>>>>> result = new HashMap<String, List<List<List<List<String>>>>>();
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		List<List<Map<String, Object>>> allLinePlatsList = new ArrayList<List<Map<String, Object>>>();
		try {
			List<Line> allLines = linePsgFlowAnalysisService.listAllLine();
			List<Map<String, Object>> allPlatForm = linePsgFlowAnalysisService.queryAllplatform();
			List<List<List<String>>> allLinePlats = new ArrayList<List<List<String>>>();
			List<List<List<List<String>>>> allPlatFormAndPlatForms = new ArrayList<List<List<List<String>>>>();// 所有点与点之间的左边集合
			for (Line l : allLines) {
				conditionMap.put("lineId", l.getId());
				List<Map<String, Object>> platFormList = linePsgFlowAnalysisService.queryplatform(conditionMap);// 查询站点坐标以及离散客流量
				allLinePlatsList.add(platFormList);
				Line oLine = lineService.selectByPrimaryKey(l.getId());
				String lPath = ClobToString((Clob) oLine.getLpath());
				List<List<String>> nexts = GpsUtils.findPlatsAndCorrectLpath(lPath, platFormList);
				allLinePlats.add(nexts);
			}
			for (int i = 0; i < allPlatForm.size() - 1; i++) {
				Map<String, Object> first = allPlatForm.get(i);
				List<String> firstLatAndLng = new ArrayList<String>();
				firstLatAndLng.add(first.get("LATITUDE").toString());
				firstLatAndLng.add(first.get("LONGITUDE").toString());
				firstLatAndLng.add("1");
				// String firstLatAndLng = first.get("LATITUDE").toString() +
				// "," + first.get("LONGITUDE").toString();
				ArrayList<List<List<String>>> latLng = new ArrayList<List<List<String>>>();// 一个点到另外一个点的坐标集合
																							// 可能有多个
				for (int j = i + 1; j < allPlatForm.size(); j++) {
					Map<String, Object> next = allPlatForm.get(j);
					List<String> nextLatAndLng = new ArrayList<String>();
					nextLatAndLng.add(next.get("LATITUDE").toString());
					nextLatAndLng.add(next.get("LONGITUDE").toString());
					nextLatAndLng.add("1");
					// String nextLatAndLng = next.get("LATITUDE").toString() +
					// "," + next.get("LONGITUDE").toString();
					List<List<String>> nexts = GpsUtils.getBetween(allLinePlats, firstLatAndLng, nextLatAndLng);
					if (!nexts.isEmpty()) {
						latLng.add(nexts);
					}
				}
				if (CollectionUtils.isNotEmpty(latLng)) {
					allPlatFormAndPlatForms.add(latLng);
				}
			}
			result.put("firstLatAndLng", allPlatFormAndPlatForms);
			resultMap.put("plats", allPlatForm);
			resultMap.put("lines", allPlatFormAndPlatForms);
			resultMap.put("platLines", allLinePlatsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	
	@RequestMapping("/queryLineDataByDepartmentId")
	@ResponseBody
	public RTResponse queryLineDataByDepartmentId(Line line) {
		RTResponse res = new RTResponse();
		res.setData(lineService.listAllLineDetail(line.getDepartmentid()));
		return res;
	}
}
