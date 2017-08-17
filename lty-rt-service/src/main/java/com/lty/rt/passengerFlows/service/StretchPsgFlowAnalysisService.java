package com.lty.rt.passengerFlows.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.basicData.bean.Stretch;
import com.lty.rt.basicData.mapper.StretchMapper;
import com.lty.rt.passengerFlows.mapper.StretchPsgflowAnalysisMapper;

/**
 * 路段客流分析统计Service
 * 
 * @author Administrator
 * 
 */
@Service
public class StretchPsgFlowAnalysisService {
	@Autowired
	private StretchMapper stretchMapper;
	@Autowired
	private StretchPsgflowAnalysisMapper stretchPsgflowAnalysisMapper;
	private static final String STRETCH_DATA = "stretchData";
	private static final String STATION_DATA = "stationData";

	public List<Stretch> listAllStretch() {
		return stretchMapper.findListByMap(null);
	}

	public Map<String, Object> queryStationFlowForHours(String stretchCode, String beginDate, String endDate,
			String holidayFlag) {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> stationData = stretchPsgflowAnalysisMapper.queryPsgFlowForHours2(stretchCode,
				beginDate, endDate, holidayFlag);
		result.put(STATION_DATA, stationData);
		List<Map<String, Object>> linedata = stretchPsgflowAnalysisMapper.queryPsgFlowForHours(stretchCode, beginDate,
				endDate, holidayFlag);
		result.put(STRETCH_DATA, linedata);

		return result;
	}

	public Map<String, Object> queryPsgFlowForDays(String stretchCode, String beginDate, String endDate,
			String beginDate2, String endDate2, String holidayFlag, String beginTime, String endTime) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> stationData = stretchPsgflowAnalysisMapper.queryPsgFlowForDays2(stretchCode,
				beginDate, endDate, beginDate2, endDate2, holidayFlag,beginTime,endTime);
		result.put(STATION_DATA, stationData);
		List<Map<String, Object>> linedata = stretchPsgflowAnalysisMapper.queryPsgFlowForDays(stretchCode, beginDate,
				endDate, beginDate2, endDate2, holidayFlag,beginTime,endTime);
		result.put(STRETCH_DATA, linedata);
		return result;

	}

	public Map<String, Object> queryPsgFlowForWeeks(String stretchCode, String beginDate, String endDate,
			String beginDate2, String endDate2, String holidayFlag, String beginTime, String endTime) {

		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> stationData = stretchPsgflowAnalysisMapper.queryPsgFlowForWeeks2(stretchCode,
				beginDate, endDate, beginDate2, endDate2, holidayFlag,beginTime,endTime);
		result.put(STATION_DATA, stationData);

		List<Map<String, Object>> linedata = stretchPsgflowAnalysisMapper.queryPsgFlowForWeeks(stretchCode, beginDate,
				endDate, beginDate2, endDate2, holidayFlag,beginTime,endTime);
		result.put(STRETCH_DATA, linedata);
		return result;

	}

	public Map<String, Object> queryPsgFlowForMonths(String stretchCode, String beginDate, String endDate,
			String beginDate2, String endDate2, String holidayFlag, String beginTime, String endTime) {

		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> stationData = stretchPsgflowAnalysisMapper.queryPsgFlowForMonths2(stretchCode,
				beginDate, endDate, beginDate2, endDate2, holidayFlag,beginTime,endTime);
		result.put(STATION_DATA, stationData);

		List<Map<String, Object>> linedata = stretchPsgflowAnalysisMapper.queryPsgFlowForMonths(stretchCode, beginDate,
				endDate, beginDate2, endDate2, holidayFlag,beginTime,endTime);
		result.put(STRETCH_DATA, linedata);
		return result;

	}

	public Map<String, Object> getAllSectionPsgFlowDataList(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String dataType = map.get("dataType") == null ? "" : map.get("dataType").toString();
		String stretchCode = map.get("stretchCode") == null ? "" : map.get("stretchCode").toString();
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		String endDate = map.get("endTime") == null ? "" : map.get("endTime").toString();
		String holidayFlag = map.get("holidayFlag") == null ? "" : map.get("holidayFlag").toString();
		String aloneHour = map.get("aloneHour") == null ? "" : map.get("aloneHour").toString();
		String aloneDDay = map.get("aloneDDay") == null ? "" : map.get("aloneDDay").toString();
		String aloneDay = map.get("aloneDay") == null ? "" : map.get("aloneDay").toString();
		String aloneMonth = map.get("aloneMonth") == null ? "" : map.get("aloneMonth").toString();
		String aloneWeek = map.get("aloneWeek") == null ? "" : map.get("aloneWeek").toString();
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("stretchCode", stretchCode);
		conditionMap.put("dataType", dataType);
		conditionMap.put("holidayFlag", holidayFlag);
		conditionMap.put("startTime", beginDate);
		conditionMap.put("endTime", endDate);
		conditionMap.put("aloneHour", aloneHour);
		conditionMap.put("aloneDDay", aloneDDay);
		conditionMap.put("aloneDay", aloneDay);
		conditionMap.put("aloneWeek", aloneWeek);
		conditionMap.put("aloneMonth", aloneMonth);
		try {
			if (("0".equals(dataType) && StringUtils.isNotBlank(aloneDay))
					|| ("1".equals(dataType) && StringUtils.isNotBlank(aloneDay))
					|| ("3".equals(dataType) && StringUtils.isNotBlank(aloneMonth))
					|| ("2".equals(dataType) && StringUtils.isNotBlank(aloneWeek))
					|| (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate))) {
				List<Map<String, Object>> sectionPsg = stretchPsgflowAnalysisMapper
						.queryLineSectionPsgFlow(conditionMap);
				// List<List<String>> newSectionPsg = new
				// ArrayList<List<String>>();
				// 经纬度纠偏
				/*
				 * if (CollectionUtils.isNotEmpty(sectionPsg)) { for
				 * (Map<String, Object> m : sectionPsg) { double lat =
				 * Double.parseDouble(); double lng = Double.parseDouble();
				 * double[] latlon = GpsCorrect.gcjDecrypt(lat, lng); } }
				 */
				result.put("sectionLinePsgData", sectionPsg);
			} else {
				result.put("sectionLinePsgData", null);
			}
			if (StringUtils.isNoneBlank(stretchCode)) {
				Map<String, Object> stretchPsg = stretchPsgflowAnalysisMapper.querySectionPsgFlow(conditionMap);
				if (stretchPsg != null) {
					String stretchName = stretchPsg.get("LINENAME") == null ? ""
							: stretchPsg.get("LINENAME").toString();
					String TOTAL_PERSON_COUNT = stretchPsg.get("TOTAL_PERSON_COUNT") == null ? "0"
							: stretchPsg.get("TOTAL_PERSON_COUNT").toString();
					List<Map<String, Object>> stretchLats = stretchPsgflowAnalysisMapper
							.queryPlatByStretchCode(stretchCode);
					List<Object> resultList = new ArrayList<Object>();
					resultList.add(stretchName);
					resultList.add(TOTAL_PERSON_COUNT);
					resultList.add(stretchLats);
					result.put("stretchPsgAndLats", resultList);
				} else {
					result.put("stretchPsgAndLats", null);
				}
			} else {
				List<Stretch> allStretch = stretchMapper.findListByMap(null);
				List<List<Object>> allStretchLats = new ArrayList<List<Object>>();
				for (Stretch stre : allStretch) {
					conditionMap.put("stretchCode", stre.getLineid());
					Map<String, Object> stretchPsg = stretchPsgflowAnalysisMapper.querySectionPsgFlow(conditionMap);
					List<Map<String, Object>> stretchLats = stretchPsgflowAnalysisMapper
							.queryPlatByStretchCode(stre.getLineid());
					if (stretchPsg != null) {
						String stretchName = stretchPsg.get("LINENAME") == null ? ""
								: stretchPsg.get("LINENAME").toString();
						String TOTAL_PERSON_COUNT = stretchPsg.get("TOTAL_PERSON_COUNT") == null ? "0"
								: stretchPsg.get("TOTAL_PERSON_COUNT").toString();
						List<Object> resultList = new ArrayList<Object>();
						resultList.add(stretchName);
						resultList.add(TOTAL_PERSON_COUNT);
						resultList.add(stretchLats);
						allStretchLats.add(resultList);
					}
				}
				result.put("stretchPsgAndLats", allStretchLats);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
}
