package com.lty.rt.passengerFlows.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.basicData.bean.Line;
import com.lty.rt.basicData.mapper.LineMapper;
import com.lty.rt.passengerFlows.mapper.AreaPsgflowAnalysisMapper;

/**
 * 小区客流分析统计Service
 * 
 * @author Administrator
 * 
 */
@Service
public class AreaPsgFlowAnalysisService {
	@Autowired
	private LineMapper lineMapper;
	@Autowired
	private AreaPsgflowAnalysisMapper areaPsgflowAnalysisMapper;
	private static final String AREA_DATA = "areaData";
	private static final String STATION_DATA = "stationData";

	public List<Line> listAllLine() {
		return lineMapper.listAllLine();
	}

	public Map<String, Object> queryStationFlowForHours(String areaCode, String beginDate, String endDate, String inlineRadioOptions, String targetAreaCode) {
		Map<String, Object> result = new HashMap<String, Object>();

		if(StringUtils.isNotBlank(inlineRadioOptions) && ("option1").equals(inlineRadioOptions)){//小区集散量
			List<Map<String, Object>> stationData = areaPsgflowAnalysisMapper.queryPsgFlowForHours2(areaCode, beginDate,
					endDate);
			result.put(STATION_DATA, stationData);
			List<Map<String, Object>> linedata = areaPsgflowAnalysisMapper.queryPsgFlowForHours(areaCode, beginDate,
					endDate);
			result.put(AREA_DATA, fillHours(linedata,2));
		}else{
			List<Map<String, Object>> stationData = areaPsgflowAnalysisMapper.queryPsgFlowForHours2OD(areaCode, beginDate,
					endDate, targetAreaCode);
			result.put(STATION_DATA, stationData);
			/*List<Map<String, Object>> linedata = areaPsgflowAnalysisMapper.queryPsgFlowForHours2OD(areaCode, beginDate,
					endDate, targetAreaCode);*/
			List<Map<String, Object>> linedata = fillHours(stationData, 1);
			result.put(AREA_DATA, linedata);
		}
		

		return result;
	}

	public Map<String, Object> queryPsgFlowForDays(String areaCode, String beginDate, String endDate, String beginDate2,
			String endDate2, String inlineRadioOptions, String targetAreaCode, String iswork, String beginTime, String endTime) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(inlineRadioOptions) && ("option1").equals(inlineRadioOptions)){//小区集散量
			List<Map<String, Object>> stationData = areaPsgflowAnalysisMapper.queryPsgFlowForDays2(areaCode, beginDate,
					endDate, beginDate2, endDate2, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);
			List<Map<String, Object>> linedata = areaPsgflowAnalysisMapper.queryPsgFlowForDays(areaCode, beginDate, endDate,
					beginDate2, endDate2, iswork,beginTime,endTime);
			result.put(AREA_DATA, linedata);
		}else{
			List<Map<String, Object>> stationData = areaPsgflowAnalysisMapper.queryPsgFlowForDays2OD(areaCode, beginDate,
					endDate, beginDate2, endDate2, targetAreaCode, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);
			/*List<Map<String, Object>> linedata = areaPsgflowAnalysisMapper.queryPsgFlowForDays2OD(areaCode, beginDate, endDate,
					beginDate2, endDate2, targetAreaCode);*/
			result.put(AREA_DATA, stationData);
		}
		
		return result;

	}

	public Map<String, Object> queryPsgFlowForWeeks(String areaCode, String beginDate, String endDate,
			String beginDate2, String endDate2, String inlineRadioOptions, String targetAreaCode, String iswork, String beginTime, String endTime) {

		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(inlineRadioOptions) && ("option1").equals(inlineRadioOptions)){//小区集散量
			List<Map<String, Object>> stationData = areaPsgflowAnalysisMapper.queryPsgFlowForWeeks2(areaCode, beginDate,
					endDate, beginDate2, endDate2, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			List<Map<String, Object>> linedata = areaPsgflowAnalysisMapper.queryPsgFlowForWeeks(areaCode, beginDate,
					endDate, beginDate2, endDate2, iswork,beginTime,endTime);
			result.put(AREA_DATA, linedata);
		}else{
			List<Map<String, Object>> stationData = areaPsgflowAnalysisMapper.queryPsgFlowForWeeks2OD(areaCode, beginDate,
					endDate, beginDate2, endDate2, targetAreaCode, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			/*List<Map<String, Object>> linedata = areaPsgflowAnalysisMapper.queryPsgFlowForWeeksOD(areaCode, beginDate,
					endDate, beginDate2, endDate2, targetAreaCode);*/
			result.put(AREA_DATA, stationData);
		}
		
		return result;

	}

	public Map<String, Object> queryPsgFlowForMonths(String areaCode, String beginDate, String endDate,
			String beginDate2, String endDate2, String inlineRadioOptions, String targetAreaCode, String iswork, String beginTime, String endTime) {

		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(inlineRadioOptions) && ("option1").equals(inlineRadioOptions)){//小区集散量
			List<Map<String, Object>> stationData = areaPsgflowAnalysisMapper.queryPsgFlowForMonths2(areaCode, beginDate,
					endDate, beginDate2, endDate2, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			List<Map<String, Object>> linedata = areaPsgflowAnalysisMapper.queryPsgFlowForMonths(areaCode, beginDate,
					endDate, beginDate2, endDate2, iswork,beginTime,endTime);
			result.put(AREA_DATA, linedata);
		}else{
			List<Map<String, Object>> stationData = areaPsgflowAnalysisMapper.queryPsgFlowForMonths2OD(areaCode, beginDate,
					endDate, beginDate2, endDate2, targetAreaCode, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			/*List<Map<String, Object>> linedata = areaPsgflowAnalysisMapper.queryPsgFlowForMonthsOD(areaCode, beginDate,
					endDate, beginDate2, endDate2, targetAreaCode);*/
			result.put(AREA_DATA, stationData);
		}
		
		return result;

	}

	public Map<String, Object> queryAreaPsgFlow(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");
		String yztId = (String) map.get("sAreaId");
		String mbztId = (String) map.get("tAreaId");
		if (StringUtils.isNotBlank(yztId) && StringUtils.isNotBlank(mbztId) && StringUtils.isNotBlank(startTime)
				&& StringUtils.isNotBlank(endTime)) {
			Map<String, Object> data = areaPsgflowAnalysisMapper.queryAreaPsgFlow(map);
			List<Map<String, Object>> yztList = areaPsgflowAnalysisMapper.queryPlatByAreaId(yztId);
			List<Map<String, Object>> mbztList = areaPsgflowAnalysisMapper.queryPlatByAreaId(mbztId);
			result.put("data", data.size() > 0 ? data : 0);
			result.put("yzt", yztList);
			result.put("mbzt", mbztList);
		}
		return result;
	}
	
	/**
	 * 补全小时
	 * @param linedata
	 * @param type 1、2
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map<String, Object>> fillHours(List<Map<String, Object>> linedata, Integer type){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		//将两天的数据分开，key为时间
		Map<String, List<Map<String, Object>>> tempMap = new HashMap<String, List<Map<String, Object>>>();
		Set<String> set = new HashSet<String>();
		Set set1 = new HashSet<String>();
		Set set2 = new HashSet<String>();
		if(linedata != null && linedata.size() > 0){
			
			//遍历得到一个Map，key为时间，value为所有的值
			for(Map<String, Object> map : linedata){
				if(tempMap.isEmpty()){
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					list.add(map);
					set1.add((String)map.get("HH"));
					tempMap.put((String)map.get("DD"), list);
				}else {
					if(tempMap.containsKey((String)map.get("DD"))){
						tempMap.get((String)map.get("DD")).add(map);
					}else{
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						list.add(map);
						set2.add((String)map.get("HH"));
						tempMap.put((String)map.get("DD"), list);
					}
				}
				set.add((String)map.get("HH"));
			}
			
			if(tempMap.size() > 1){
				//补0到linedata
				for (Map.Entry<String, List<Map<String, Object>>> entry : tempMap.entrySet()) {
					List<Map<String, Object>> list = entry.getValue();
					for(String str : set) {
						Boolean flag = false;
						for(Map<String, Object> map : list){
							if(str.equals((String)map.get("HH"))){
								flag = true;
							}
						}
						if(!flag){
							if(type != null && type == 1){
								Map<String, Object> zeroMap = createZeroMapOne(list.get(0));
								zeroMap.put("DD",  entry.getKey());
								zeroMap.put("HH", str);
								tempMap.get(entry.getKey()).add(zeroMap);
							}else if(type != null && type == 2){
								Map<String, Object> zeroMap = createZeroMapTwo(list.get(0));
								zeroMap.put("DD",  entry.getKey());
								zeroMap.put("HH", str);
								tempMap.get(entry.getKey()).add(zeroMap);
							}
						}
					}
				}

				//将tempMap的value排序			
				for (Map.Entry<String, List<Map<String, Object>>> entry : tempMap.entrySet()) {
					resultList.addAll(compareSort(entry.getValue()));
				}
			}else{
				resultList.addAll(linedata);
			}
			
		}
		
		return resultList;
	}
	
	
	private Map<String, Object> createZeroMapOne(Map<String, Object> tempMap){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ONBUS_PERSON_COUNT", 0);
		map.put("TOTAL_PERSON_COUNT", 0);
		map.put("TAGET_AREA_NAME", tempMap.get("TAGET_AREA_NAME"));
		map.put("AREA_NAME", tempMap.get("AREA_NAME"));
		return map;
	}
	
	private Map<String, Object> createZeroMapTwo(Map<String, Object> tempMap){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ONBUS_PERSON_COUNT", 0);
		map.put("TOTAL_PERSON_COUNT", 0);
		map.put("AREA_NAME", tempMap.get("AREA_NAME"));
		return map;
	}
	
	
	private List<Map<String, Object>> compareSort(List<Map<String, Object>> list) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String name1 = (String) o1.get("HH");// name1是从你list里面拿出来的一个
				String name2 = (String) o2.get("HH"); // name1是从你list里面拿出来的第二个name
				return name1.compareTo(name2);
			}
		});
		return list;
	}
}
