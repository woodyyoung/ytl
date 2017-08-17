package com.lty.rt.passengerFlows.mapper;

import java.util.List;
import java.util.Map;

public interface IndexPsgFlowMapper {

	Map<String, Object> queryTotalPsgFlowForToday();

	Map<String, Object> queryMaxStationAndCommunityPsgFlowForToday();

	List<Map<String, Object>> queryVolatilityPsgFlow();

	Map<String, Object> queryMaxCrowdedPsgFlowForToday();

	Map<String, Object> queryAVGCrowdedPsgFlow();

	List<Map<String, Object>> queryTopCrowdedPsgFlow();

	List<Map<String, Object>> queryStaionsPsgFlow();

	List<Map<String, Object>> queryTopPlatformPsgFlowForToday();

	List<Map<String, Object>> queryTopCommunityPsgFlowForToday();

}