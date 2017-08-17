package com.lty.rt.passengerFlows.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lty.rt.passengerFlows.bean.StationPsgflow;

public interface StationPsgflowMapper {
	int deleteByPrimaryKey(String id);

	int insert(StationPsgflow record);

	int insertSelective(StationPsgflow record);

	StationPsgflow selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(StationPsgflow record);

	int updateByPrimaryKey(StationPsgflow record);

	List<Map<String, Object>> queryStationODPsgFlow(Map<String, Object> params);

	List<Map<String, Object>> queryStationDefaultPsgFlow(
			Map<String, Object> params);

	List<Map<String, Object>> queryPsgFlow(
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate,
			@Param("queryPlatforms") String queryPlatforms,
			@Param("holidayFlag") String holidayFlag,
			@Param("platformDirection") String platformDirection);

	List<Map<String, Object>> queryStationsPsgFlow(
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate,
			@Param("queryYPlatform") String queryYPlatform,
			@Param("queryTPlatform") String queryTPlatform,
			@Param("holidayFlag") String holidayFlag);

	List<Map<String, Object>> queryPsgFlowForDays(
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2,
			@Param("endDate2") String endDate2,
			@Param("queryPlatforms") String queryPlatforms,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime,
			@Param("holidayFlag") String holidayFlag,
			@Param("platformDirection") String platformDirection);

	List<Map<String, Object>> queryStationsPsgFlowForDays(
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2,
			@Param("endDate2") String endDate2,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime,
			@Param("queryYPlatform") String queryYPlatform,
			@Param("queryTPlatform") String queryTPlatform,
			@Param("holidayFlag") String holidayFlag);

	List<Map<String, Object>> queryPsgFlowForWeeks(
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2,
			@Param("endDate2") String endDate2,
			@Param("queryPlatforms") String queryPlatforms,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime,
			@Param("platformDirection") String platformDirection);

	List<Map<String, Object>> queryStationsPsgFlowForWeeks(
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2,
			@Param("endDate2") String endDate2,
			@Param("queryYPlatform") String queryYPlatform,
			@Param("queryTPlatform") String queryTPlatform,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime);

	List<Map<String, Object>> queryPsgFlowForMonths(
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2,
			@Param("endDate2") String endDate2,
			@Param("queryPlatforms") String queryPlatforms,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime,
			@Param("platformDirection") String platformDirection);

	List<Map<String, Object>> queryStationsPsgFlowForMonths(
			@Param("beginDate") String beginDate,
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2,
			@Param("endDate2") String endDate2,
			@Param("queryYPlatform") String queryYPlatform,
			@Param("queryTPlatform") String queryTPlatform,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime);

}