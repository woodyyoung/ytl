package com.lty.rt.passengerFlows.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AreaPsgflowAnalysisMapper {
	
	List<Map<String, Object>> queryAreaODPsgFlow(Map<String, Object> params);
	
	List<Map<String, Object>> queryAreaDefaultPsgFlow(Map<String, Object> params);
	

	List<Map<String, Object>> queryPsgFlowForHours(@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, @Param("endDate") String endDate);

	List<Map<String, Object>> queryPsgFlowForHours2(@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, @Param("endDate") String endDate);
	
	List<Map<String, Object>> queryPsgFlowForHours2OD(@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("targetAreaCode") String targetAreaCode);

	List<Map<String, Object>> queryPsgFlowForDays(
			@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);
	
	List<Map<String, Object>> queryPsgFlowForDays2(
			@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);
	
	List<Map<String, Object>> queryPsgFlowForDays2OD(
			@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2, 
			@Param("targetAreaCode") String targetAreaCode, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForWeeks(
			@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);
	
	List<Map<String, Object>> queryPsgFlowForWeeks2(
			@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);
	
	List<Map<String, Object>> queryPsgFlowForWeeks2OD(
			@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2, 
			@Param("targetAreaCode") String targetAreaCode, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForMonths(
			@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
			);

	List<Map<String, Object>> queryPsgFlowForMonths2(
			@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);
	
	List<Map<String, Object>> queryPsgFlowForMonths2OD(
			@Param("areaCode") String areaCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2, 
			@Param("targetAreaCode") String targetAreaCode, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
			);

	Map<String, Object> queryAreaPsgFlow(Map<String, Object> map);

	List<Map<String, Object>> queryPlatByAreaId(@Param("areaId") String areaId);
}