package com.lty.rt.passengerFlows.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lty.rt.passengerFlows.bean.PlatFormPsgFlow;

public interface LinePsgflowAnalysisMapper {
	
	List<Map<String, Object>> queryLineODPsgFlow(Map<String, Object> params);
	
	List<Map<String, Object>> queryLineDefaultPsgFlow(Map<String, Object> params);
	

	List<Map<String, Object>> queryPsgFlowForHours(@Param("lineId") String lineId, @Param("beginDate") String beginDate,
			@Param("endDate") String endDate, @Param("stationId") String stationId);

	List<Map<String, Object>> queryPsgFlowForHoursOD(@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, @Param("endDate") String endDate,
			@Param("stationId") String stationId, @Param("targetStationId") String targetStationId);

	List<Map<String, Object>> queryStationPsgFlowByLineId(Map<String, Object> conditionMap);

	List<Map<String, Object>> queryPlatFormPsgFlowByLineId(Map<String, Object> conditionMap);

	List<Map<String, Object>> queryPsgFlowForHourToHour(Map<String, Object> map);

	List<Map<String, Object>> queryStationTopPsgFlowForHourToHour(Map<String, Object> map);

	List<Map<String, Object>> queryStationTopPsgFlowForDayToDay(Map<String, Object> map);

	List<Map<String, Object>> queryLineTopPsgFlowForHourToHour(Map<String, Object> map);

	List<Map<String, Object>> queryLineTopPsgFlowForDayToDay(Map<String, Object> map);

	List<Map<String, Object>> queryPsgFlowForHours2(@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, @Param("endDate") String endDate,
			@Param("stationId") String stationId);

	List<Map<String, Object>> queryPsgFlowForHours2OD(@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, @Param("endDate") String endDate,
			@Param("stationId") String stationId, @Param("targetStationId") String targetStationId);

	List<Map<String, Object>> queryPsgFlowForDays(
			@Param("lineId") String lineId, 
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate, 
			@Param("beginDate2") String beginDate2,
			@Param("endDate2") String endDate2, 
			@Param("stationId") String stationId, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForDaysOD(
			@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("stationId") String stationId, 
			@Param("targetStationId") String targetStationId,
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForDayToDay(Map<String, Object> map);

	List<Map<String, Object>> queryPsgFlowForDays2(
			@Param("lineId") String lineId, 
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate, 
			@Param("beginDate2") String beginDate2,
			@Param("endDate2") String endDate2, 
			@Param("stationId") String stationId, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForDays2OD(
			@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("stationId") String stationId, 
			@Param("targetStationId") String targetStationId,
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForWeeks(
			@Param("lineId") String lineId, 
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate, 
			@Param("beginDate2") String beginDate2,
			@Param("endDate2") String endDate2, 
			@Param("stationId") String stationId, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForWeeksOD(
			@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("stationId") String stationId, 
			@Param("targetStationId") String targetStationId,
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForWeekToWeek(Map<String, Object> map);

	List<Map<String, Object>> queryLineTopPsgFlowForMonthToMonth(Map<String, Object> map);

	List<Map<String, Object>> queryLineTopPsgFlowForWeekToWeek(Map<String, Object> map);

	List<Map<String, Object>> queryPsgFlowForWeeks2(
			@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("stationId") String stationId, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForWeeks2OD(
			@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("stationId") String stationId, 
			@Param("targetStationId") String targetStationId,
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForMonths(
			@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("stationId") String stationId, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForMonthsOD(
			@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("stationId") String stationId, 
			@Param("targetStationId") String targetStationId,
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForMonthToMonth(Map<String, Object> map);

	List<Map<String, Object>> queryPsgFlowForMonths2(
			@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("stationId") String stationId, 
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForMonths2OD(
			@Param("lineId") String lineId,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("stationId") String stationId, 
			@Param("targetStationId") String targetStationId,
			@Param("iswork") String iswork,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	Map<String, Object> queryTotalPsgFlowForToday();

	Map<String, Object> queryMaxLinePsgFlowForToday();

	Map<String, Object> queryMaxStationPsgFlowForToday();

	Map<String, Object> queryMaxCommunityPsgFlowForToday();

	List<Map<String, Object>> queryStationTopPsgFlowForWeekToWeek(Map<String, Object> map);

	List<Map<String, Object>> queryStationTopPsgFlowForMonthToMonth(Map<String, Object> map);

	List<Map<String, Object>> queryplatform(Map<String, Object> conditionMap);

	List<Map<String, Object>> queryAllplatform();

	List<Map<String, Object>> queryPlatFormPsgFlow(Map<String, Object> map);

	List<Map<String, Object>> queryPlatFormPsgFlowByMap(Map<String, Object> conditionMap);

	List<PlatFormPsgFlow> getAllPlatFormData(Map<String, Object> conditionMap);

	List<Map<String, Object>> getLinePsgByPlatFormId(Map<String, Object> conditionMap);
}