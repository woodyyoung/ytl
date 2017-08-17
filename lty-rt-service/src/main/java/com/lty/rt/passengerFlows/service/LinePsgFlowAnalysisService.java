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
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.passengerFlows.bean.PlatFormPsgFlow;
import com.lty.rt.passengerFlows.mapper.LinePsgflowAnalysisMapper;

/**
 * 线路客流分析统计Service
 * 
 * @author Administrator
 * 
 */
@Service
public class LinePsgFlowAnalysisService {
	@Autowired
	private LineMapper lineMapper;
	@Autowired
	private LinePsgflowAnalysisMapper linePsgflowAnalysisMapper;
	private static final String LINE_DATA = "lineData";
	private static final String LINE_TopDATA = "lineTopData";
	private static final String STATION_DATA = "stationData";
	private static final String STATION_TopDATA = "stationTopData";

	public List<Line> listAllLine() {
		return lineMapper.listAllLine();
	}

	// --line xx to xx begin --

	// --line xx to xx begin --

	public List<Map<String, Object>> queryStationPsgFlowByLineId(Map<String, Object> conditionMap) {
		List<Map<String, Object>> stationData = null;
		String lineId = (String) conditionMap.get("lineId");
		if (StringUtils.isNotBlank(lineId)) {
			stationData = linePsgflowAnalysisMapper.queryStationPsgFlowByLineId(conditionMap);
		} else {

		}
		return stationData;
	}

	public List<Map<String, Object>> queryPlatFormPsgFlowByLineId(Map<String, Object> conditionMap) {
		List<Map<String, Object>> stationData = null;
		String lineId = (String) conditionMap.get("lineId");
		if (StringUtils.isNotBlank(lineId)) {
			stationData = linePsgflowAnalysisMapper.queryPlatFormPsgFlowByLineId(conditionMap);
		}
		return stationData;
	}

	public Map<String, Object> queryPsgFlowForHourToHour(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String lineId = map.get("lineId") == null ? "" : map.get("lineId").toString();
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		if (StringUtils.isNotBlank(lineId) && StringUtils.isNotBlank(beginDate)) {
			List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForHourToHour(map);
			result.put(LINE_DATA, linedata);
		} else {
			result.put(LINE_DATA, null);
		}
		List<Map<String, Object>> lineTopData = linePsgflowAnalysisMapper.queryLineTopPsgFlowForHourToHour(map);
		result.put(LINE_TopDATA, lineTopData);
		List<Map<String, Object>> stationTopData = linePsgflowAnalysisMapper.queryStationTopPsgFlowForHourToHour(map);
		result.put(STATION_TopDATA, stationTopData);
		return result;
	}

	// 查询所有线路的时客流量
	public Map<String, Object> queryAllPsgFlowForHourToHour(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		if (StringUtils.isNotBlank(beginDate)) {
			List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForHourToHour(map);
			result.put(LINE_DATA, linedata);
		} else {
			result.put(LINE_DATA, null);
		}
		return result;
	}

	public Map<String, Object> queryPsgFlowForDayToDay(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String lineId = map.get("lineId") == null ? "" : map.get("lineId").toString();
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		String endDate = map.get("endTime") == null ? "" : map.get("endTime").toString();
		if (StringUtils.isNotBlank(lineId) && StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
			List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForDayToDay(map);
			result.put(LINE_DATA, linedata);
		} else {
			result.put(LINE_DATA, null);
		}
		List<Map<String, Object>> lineTopData = linePsgflowAnalysisMapper.queryLineTopPsgFlowForDayToDay(map);
		result.put(LINE_TopDATA, lineTopData);
		List<Map<String, Object>> stationTopData = linePsgflowAnalysisMapper.queryStationTopPsgFlowForDayToDay(map);
		result.put(STATION_TopDATA, stationTopData);
		return result;
	}

	// 查询所有线路的日客流量
	public Map<String, Object> queryAllPsgFlowForDayToDay(Map<String, Object> map) {
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		String endDate = map.get("endTime") == null ? "" : map.get("endTime").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
			List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForDayToDay(map);
			result.put(LINE_DATA, linedata);
		} else {
			result.put(LINE_DATA, null);
		}
		return result;
	}

	public Map<String, Object> queryPsgFlowForWeekToWeek(Map<String, Object> map) {
		String lineId = map.get("lineId") == null ? "" : map.get("lineId").toString();
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		String endDate = map.get("endTime") == null ? "" : map.get("endTime").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(lineId) && StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
			List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForWeekToWeek(map);
			result.put(LINE_DATA, linedata);
		} else {
			result.put(LINE_DATA, null);
		}
		List<Map<String, Object>> lineTopData = linePsgflowAnalysisMapper.queryLineTopPsgFlowForWeekToWeek(map);
		result.put(LINE_TopDATA, lineTopData);
		List<Map<String, Object>> stationTopData = linePsgflowAnalysisMapper.queryStationTopPsgFlowForWeekToWeek(map);
		result.put(STATION_TopDATA, stationTopData);
		return result;
	}

	// 查询所有线路的周客流量
	public Map<String, Object> queryAllPsgFlowForWeekToWeek(Map<String, Object> map) {
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		String endDate = map.get("endTime") == null ? "" : map.get("endTime").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
			List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForWeekToWeek(map);
			result.put(LINE_DATA, linedata);
		} else {
			result.put(LINE_DATA, null);
		}
		return result;
	}

	public Map<String, Object> queryPsgFlowForMonthToMonth(Map<String, Object> map) {
		String lineId = map.get("lineId") == null ? "" : map.get("lineId").toString();
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		String endDate = map.get("endTime") == null ? "" : map.get("endTime").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(lineId) && StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
			List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForMonthToMonth(map);
			result.put(LINE_DATA, linedata);
		} else {
			result.put(LINE_DATA, null);
		}
		List<Map<String, Object>> lineTopData = linePsgflowAnalysisMapper.queryLineTopPsgFlowForMonthToMonth(map);
		result.put(LINE_TopDATA, lineTopData);
		List<Map<String, Object>> stationTopData = linePsgflowAnalysisMapper.queryStationTopPsgFlowForMonthToMonth(map);
		result.put(STATION_TopDATA, stationTopData);
		return result;
	}

	// 查询通过该站台的个线路客流占有率
	public List<Map<String, Object>> queryPlatFormPsgFlow(Map<String, Object> map) {
		List<Map<String, Object>> result = linePsgflowAnalysisMapper.queryPlatFormPsgFlow(map);
		return result;
	}

	// 查询所有线路的月客流量
	public Map<String, Object> queryAllPsgFlowForMonthToMonth(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String beginDate = map.get("startTime") == null ? "" : map.get("startTime").toString();
		String endDate = map.get("endTime") == null ? "" : map.get("endTime").toString();
		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
			List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForMonthToMonth(map);
			result.put(LINE_DATA, linedata);
		} else {
			result.put(LINE_DATA, null);
		}
		return result;
	}
	// --line xx to xx end --

	public Map<String, Object> queryStationFlowForHours(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String beginDate = map.get("beginDate") == null ? "" : map.get("beginDate").toString();
		String endDate = map.get("endDate") == null ? "" : map.get("endDate").toString();
		String lineId = map.get("lineId") == null ? "" : map.get("lineId").toString();
		String stationId = map.get("stationId") == null ? "" : map.get("stationId").toString();
		String targetStationId = map.get("targetStationId") == null ? "" : map.get("targetStationId").toString();
		String inlineRadioOptions = map.get("inlineRadioOptions") == null ? ""
				: map.get("inlineRadioOptions").toString();

		if (StringUtils.isNotBlank(inlineRadioOptions) && ("option1").equals(inlineRadioOptions)) {// 站台集散量
			List<Map<String, Object>> stationData = linePsgflowAnalysisMapper.queryPsgFlowForHours2(lineId, beginDate,
					endDate, stationId);
			result.put(STATION_DATA, stationData);
			if (StringUtils.isBlank(stationId)) {
				List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForHours(lineId, beginDate,
						endDate, stationId);
				result.put(LINE_DATA, fillHours(linedata, 2));
			} else {
				result.put(LINE_DATA, fillHours(stationData, 2));
			}
		} else if (StringUtils.isNotBlank(inlineRadioOptions) && ("option2").equals(inlineRadioOptions)) {// 站台间客流
			if (StringUtils.isBlank(lineId)) {
				throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":lineId");
			}
			if (StringUtils.isBlank(targetStationId)) {
				throw new ApplicationException(ReturnCode.ERROR_03.getCode(),
						ReturnCode.ERROR_03.getMsg() + ":targetStationId");
			}
			List<Map<String, Object>> stationData = linePsgflowAnalysisMapper.queryPsgFlowForHours2OD(lineId, beginDate,
					endDate, stationId, targetStationId);
			result.put(STATION_DATA, stationData);
			if (StringUtils.isBlank(stationId)) {
				List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForHoursOD(lineId, beginDate,
						endDate, stationId, targetStationId);
				result.put(LINE_DATA, fillHours(linedata, 1));
			} else {
				result.put(LINE_DATA, fillHours(stationData, 1));
			}
		}

		return result;
	}

	/**
	 * 补全小时
	 * 
	 * @param linedata
	 * @param type
	 *            1,2
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map<String, Object>> fillHours(List<Map<String, Object>> linedata, Integer type) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 将两天的数据分开，key为时间
		Map<String, List<Map<String, Object>>> tempMap = new HashMap<String, List<Map<String, Object>>>();
		Set<String> set = new HashSet<String>();
		Set set1 = new HashSet<String>();
		Set set2 = new HashSet<String>();
		if (linedata != null && linedata.size() > 0) {

			// 遍历得到一个Map，key为时间，value为所有的值
			for (Map<String, Object> map : linedata) {
				if (tempMap.isEmpty()) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					list.add(map);
					set1.add((String) map.get("HH"));
					tempMap.put((String) map.get("DD"), list);
				} else {
					if (tempMap.containsKey((String) map.get("DD"))) {
						tempMap.get((String) map.get("DD")).add(map);
					} else {
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						list.add(map);
						set2.add((String) map.get("HH"));
						tempMap.put((String) map.get("DD"), list);
					}
				}
				set.add((String) map.get("HH"));
			}

			if (tempMap.size() > 1) {
				// 补0到linedata
				for (Map.Entry<String, List<Map<String, Object>>> entry : tempMap.entrySet()) {
					List<Map<String, Object>> list = entry.getValue();
					for (String str : set) {
						Boolean flag = false;
						for (Map<String, Object> map : list) {
							if (str.equals((String) map.get("HH"))) {
								flag = true;
							}
						}
						if (!flag) {
							if (type != null && type == 1) {
								Map<String, Object> zeroMap = createZeroMapOne(list.get(0));
								zeroMap.put("DD", entry.getKey());
								zeroMap.put("HH", str);
								tempMap.get(entry.getKey()).add(zeroMap);
							} else if (type != null && type == 2) {
								Map<String, Object> zeroMap = createZeroMapTwo(list.get(0));
								zeroMap.put("DD", entry.getKey());
								zeroMap.put("HH", str);
								tempMap.get(entry.getKey()).add(zeroMap);
							}
						}
					}
				}

				// 将tempMap的value排序
				for (Map.Entry<String, List<Map<String, Object>>> entry : tempMap.entrySet()) {
					resultList.addAll(compareSort(entry.getValue()));
				}
			} else {
				resultList.addAll(linedata);
			}

		}

		return resultList;
	}

	private Map<String, Object> createZeroMapOne(Map<String, Object> tempMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ONBUS_PERSON_COUNT", 0);
		map.put("TOTAL_PERSON_COUNT", 0);
		map.put("STATION_ID", tempMap.get("STATION_ID"));
		map.put("STATION_NAME", tempMap.get("STATION_NAME"));
		map.put("LINE_ID", tempMap.get("LINE_ID"));
		map.put("LINE_NAME", tempMap.get("LINE_NAME"));
		return map;
	}

	private Map<String, Object> createZeroMapTwo(Map<String, Object> tempMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ONBUS_PERSON_COUNT", 0);
		map.put("TOTAL_PERSON_COUNT", 0);
		map.put("STATION_ID", tempMap.get("STATION_ID"));
		map.put("STATION_NAME", tempMap.get("STATION_NAME"));
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

	public Map<String, Object> queryPsgFlowForDays(String lineId, String beginDate, String endDate, String beginDate2,
			String endDate2, String stationId, String inlineRadioOptions, String targetStationId, String iswork, String beginTime, String endTime) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(inlineRadioOptions) && ("option1").equals(inlineRadioOptions)) {// 站台集散量
			List<Map<String, Object>> stationData = linePsgflowAnalysisMapper.queryPsgFlowForDays2(lineId, beginDate,
					endDate, beginDate2, endDate2, stationId, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			if (StringUtils.isBlank(stationId)) {
				List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForDays(lineId, beginDate,
						endDate, beginDate2, endDate2, stationId, iswork,beginTime,endTime);
				result.put(LINE_DATA, linedata);
			} else {
				result.put(LINE_DATA, stationData);
			}
		} else {
			if (StringUtils.isBlank(lineId)) {
				throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":lineId");
			}
			if (StringUtils.isBlank(targetStationId)) {
				throw new ApplicationException(ReturnCode.ERROR_03.getCode(),
						ReturnCode.ERROR_03.getMsg() + ":targetStationId");
			}
			List<Map<String, Object>> stationData = linePsgflowAnalysisMapper.queryPsgFlowForDays2OD(lineId, beginDate,
					endDate, beginDate2, endDate2, stationId, targetStationId, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			if (StringUtils.isBlank(stationId)) {
				List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForDaysOD(lineId, beginDate,
						endDate, beginDate2, endDate2, stationId, targetStationId, iswork,beginTime,endTime);
				result.put(LINE_DATA, linedata);
			} else {
				result.put(LINE_DATA, stationData);
			}
		}

		return result;

	}

	public Map<String, Object> queryPsgFlowForWeeks(String lineId, String beginDate, String endDate, String beginDate2,
			String endDate2, String stationId, String inlineRadioOptions, String targetStationId, String iswork, String beginTime, String endTime) {

		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(inlineRadioOptions) && ("option1").equals(inlineRadioOptions)) {// 站台集散量
			List<Map<String, Object>> stationData = linePsgflowAnalysisMapper.queryPsgFlowForWeeks2(lineId, beginDate,
					endDate, beginDate2, endDate2, stationId, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			if (StringUtils.isBlank(stationId)) {
				List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForWeeks(lineId, beginDate,
						endDate, beginDate2, endDate2, stationId, iswork,beginTime,endTime);
				result.put(LINE_DATA, linedata);
			} else {
				result.put(LINE_DATA, stationData);
			}
		} else {
			if (StringUtils.isBlank(lineId)) {
				throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":lineId");
			}
			if (StringUtils.isBlank(targetStationId)) {
				throw new ApplicationException(ReturnCode.ERROR_03.getCode(),
						ReturnCode.ERROR_03.getMsg() + ":targetStationId");
			}
			List<Map<String, Object>> stationData = linePsgflowAnalysisMapper.queryPsgFlowForWeeks2OD(lineId, beginDate,
					endDate, beginDate2, endDate2, stationId, targetStationId, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			if (StringUtils.isBlank(stationId)) {
				List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForWeeksOD(lineId, beginDate,
						endDate, beginDate2, endDate2, stationId, targetStationId, iswork,beginTime,endTime);
				result.put(LINE_DATA, linedata);
			} else {
				result.put(LINE_DATA, stationData);
			}
		}

		return result;

	}

	public Map<String, Object> queryPsgFlowForMonths(String lineId, String beginDate, String endDate, String beginDate2,
			String endDate2, String stationId, String inlineRadioOptions, String targetStationId, String iswork, String beginTime, String endTime) {

		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(inlineRadioOptions) && ("option1").equals(inlineRadioOptions)) {// 站台集散量
			List<Map<String, Object>> stationData = linePsgflowAnalysisMapper.queryPsgFlowForMonths2(lineId, beginDate,
					endDate, beginDate2, endDate2, stationId, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			if (StringUtils.isBlank(stationId)) {
				List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForMonths(lineId, beginDate,
						endDate, beginDate2, endDate2, stationId, iswork,beginTime,endTime);
				result.put(LINE_DATA, linedata);
			} else {
				result.put(LINE_DATA, stationData);
			}
		} else {
			if (StringUtils.isBlank(lineId)) {
				throw new ApplicationException(ReturnCode.ERROR_03.getCode(), ReturnCode.ERROR_03.getMsg() + ":lineId");
			}
			if (StringUtils.isBlank(targetStationId)) {
				throw new ApplicationException(ReturnCode.ERROR_03.getCode(),
						ReturnCode.ERROR_03.getMsg() + ":targetStationId");
			}
			List<Map<String, Object>> stationData = linePsgflowAnalysisMapper.queryPsgFlowForMonths2OD(lineId,
					beginDate, endDate, beginDate2, endDate2, stationId, targetStationId, iswork,beginTime,endTime);
			result.put(STATION_DATA, stationData);

			if (StringUtils.isBlank(stationId)) {
				List<Map<String, Object>> linedata = linePsgflowAnalysisMapper.queryPsgFlowForMonthsOD(lineId,
						beginDate, endDate, beginDate2, endDate2, stationId, targetStationId, iswork,beginTime,endTime);
				result.put(LINE_DATA, linedata);
			} else {
				result.put(LINE_DATA, stationData);
			}
		}

		return result;

	}

	public List<Map<String, Object>> queryplatform(Map<String, Object> conditionMap) {
		return linePsgflowAnalysisMapper.queryplatform(conditionMap);
	}

	public List<Map<String, Object>> queryAllplatform() {
		return linePsgflowAnalysisMapper.queryAllplatform();
	}

	public List<Map<String, Object>> queryPlatFormPsgFlowByMap(Map<String, Object> conditionMap) {
		return linePsgflowAnalysisMapper.queryPlatFormPsgFlowByMap(conditionMap);
	}

	public List<PlatFormPsgFlow> getAllPlatFormData(Map<String, Object> conditionMap) {
		return linePsgflowAnalysisMapper.getAllPlatFormData(conditionMap);
	}

	public List<Map<String, Object>> getLinePsgByPlatFormId(Map<String, Object> conditionMap) {
		return linePsgflowAnalysisMapper.getLinePsgByPlatFormId(conditionMap);
	}
}
