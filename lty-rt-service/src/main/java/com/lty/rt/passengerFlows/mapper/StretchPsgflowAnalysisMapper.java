package com.lty.rt.passengerFlows.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface StretchPsgflowAnalysisMapper {
	
	List<Map<String, Object>> queryStretchDefaultPsgFlow(Map<String, Object> params);

	List<Map<String, Object>> queryPsgFlowForHours(@Param("stretchCode") String stretchCode,
			@Param("beginDate") String beginDate, @Param("endDate") String endDate,
			@Param("holidayFlag") String holidayFlag);

	List<Map<String, Object>> queryPsgFlowForHours2(@Param("stretchCode") String stretchCode,
			@Param("beginDate") String beginDate, @Param("endDate") String endDate,
			@Param("holidayFlag") String holidayFlag);

	List<Map<String, Object>> queryPsgFlowForDays(
			@Param("stretchCode") String stretchCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForDays2(
			@Param("stretchCode") String stretchCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForWeeks(
			@Param("stretchCode") String stretchCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForWeeks2(
			@Param("stretchCode") String stretchCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForMonths(
			@Param("stretchCode") String stretchCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	List<Map<String, Object>> queryPsgFlowForMonths2(
			@Param("stretchCode") String stretchCode,
			@Param("beginDate") String beginDate, 
			@Param("endDate") String endDate,
			@Param("beginDate2") String beginDate2, 
			@Param("endDate2") String endDate2,
			@Param("holidayFlag") String holidayFlag,
			@Param("beginTime") String beginTime,
			@Param("endTime") String endTime
);

	Map<String, Object> querySectionPsgFlow(Map<String, Object> map);

	List<Map<String, Object>> queryPlatByStretchCode(String stretchCode);

	List<Map<String, Object>> queryLineSectionPsgFlow(Map<String, Object> map);
}