package com.lty.rt.auxiliaryAnalysis.mapper;

import java.util.List;
import java.util.Map;

public interface PsgCorridorMapper {
	/**
	 * 按天统计小区的OD客流总量
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryTotalODByday(Map<String, Object> params);
	
	/**
	 * 按天统计小区到其他各个小区的客流总量 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryToOtherAreaODByDay(Map<String, Object> params);

	/**
	 * 查询小区的信息
	 * @return
	 */
	List<Map<String, Object>> queryAreaInfo(String areaCode);

	/**
	 * 按小时查询小区站间客流
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryAreaODByHour(Map<String, Object> params);
	
	/**
	 * 按天查询小区站间客流
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryAreaODByDay(Map<String, Object> params);
	
	/**
	 * 按周查询小区站间客流
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryAreaODByWeek(Map<String, Object> params);
	
	/**
	 * 按月查询小区站间客流
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryAreaODByMonth(Map<String, Object> params);

	/**
	 * 查询小区站间总客流（天）
	 * @param params
	 * @return
	 */
	int queryTotleAreaODByDay(Map<String, Object> params);
	
	/**
	 * 查询小区站间总客流（时刻）
	 * @param params
	 * @return
	 */
	int queryTotleAreaODByTime(Map<String, Object> params);

	List<Map<String, Object>> queryFlowOutTop(Map<String, Object> params);

	List<Map<String, Object>> queryFlowInTop(Map<String, Object> params);

	List<Map<String, Object>> queryFlowAnalysisByDay(Map<String, Object> params);

	List<Map<String, Object>> queryMapAreaFlowOut(Map<String, Object> params);

	List<Map<String, Object>> queryMapAreaFlowIn(Map<String, Object> params);

	List<Map<String, Object>> queryFlowAnalysisByHour(Map<String, Object> params);

	List<Map<String, Object>> queryFlowAnalysisByMonth(Map<String, Object> params);

	List<Map<String, Object>> queryFlowAnalysisByWeek(Map<String, Object> params);
}
