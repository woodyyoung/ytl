package com.lty.rt.passengerFlows.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.comm.util.AddZeroUtils;
import com.lty.rt.passengerFlows.mapper.IndexPsgFlowMapper;

/**
 * 首页客流显示service
 * 
 * @author YUJI
 *
 */

@Service
public class IndexPsgFlowAnalysisService {
	@Autowired
	private IndexPsgFlowMapper indexPsgFlowMapper;

	public Map<String, Object> queryMaxPsgFlowData() {
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询当日客流总量
		Map<String, Object> totalPsgToday = indexPsgFlowMapper.queryTotalPsgFlowForToday();
		result.put("totalPsgToday", totalPsgToday);
		// 查询当日最拥挤线路
		Map<String, Object> maxLinePsgToday = indexPsgFlowMapper.queryMaxCrowdedPsgFlowForToday();
		result.put("maxLinePsg", maxLinePsgToday);
		// 查询平均满载率
		Map<String, Object> avgLinePsgToday = indexPsgFlowMapper.queryAVGCrowdedPsgFlow();
		result.put("avgLinePsgToday", avgLinePsgToday);
		// 查询客流最大站台与客流最大的小区
		Map<String, Object> maxStationAndCommunityPsgToday = indexPsgFlowMapper
				.queryMaxStationAndCommunityPsgFlowForToday();
		result.put("maxStationAndCommunityPsg", maxStationAndCommunityPsgToday);
		// 查询Top10线路满载率
		List<Map<String, Object>> topCrowdedPsg = indexPsgFlowMapper.queryTopCrowdedPsgFlow();
		result.put("topCrowdedPsg", topCrowdedPsg);
		// 查询Top10客流最大的站台
		List<Map<String, Object>> maxPlatformPsgToday = indexPsgFlowMapper.queryTopPlatformPsgFlowForToday();
		result.put("topPlatformPsg", maxPlatformPsgToday);
		// 查询Top10客流最大小区
		List<Map<String, Object>> maxCommunityPsgToday = indexPsgFlowMapper.queryTopCommunityPsgFlowForToday();
		result.put("topCommunityPsg", maxCommunityPsgToday);
		// 查询24小时客流波动
		List<Map<String, Object>> volatilityPsgFlow = indexPsgFlowMapper.queryVolatilityPsgFlow();
		Map<String, Object> zeroMap = new HashMap<String, Object>();
		zeroMap.put("TOTAL_PERSON_COUNT", 0);
		zeroMap.put("TOTAL_PERSON_COUNT", 0);
		List<Map<String, Object>> newvolatilityPsgFlow = AddZeroUtils.fillHours(volatilityPsgFlow, zeroMap, "HH",
				"DATATYPE");
		result.put("volatilityPsgFlow", newvolatilityPsgFlow);
		// 地图客流
		List<Map<String, Object>> staionsPsgFlow = indexPsgFlowMapper.queryStaionsPsgFlow();
		result.put("staionsPsgFlow", staionsPsgFlow);
		return result;
	}

}
