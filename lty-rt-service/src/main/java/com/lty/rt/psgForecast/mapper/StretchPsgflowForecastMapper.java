package com.lty.rt.psgForecast.mapper;

import java.util.List;
import java.util.Map;

public interface StretchPsgflowForecastMapper {
	/**
	 * 查询预测月份的客流量及历史分析时段的每个月的客流量
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryMonthData(Map<String, Object> params);

	/**
	 * 查询预测月份每一天的客流量
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryDaysData(Map<String, Object> params);

	/**
	 * 查询预测站台预测某一天的客流量
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryStretchsData(Map<String, Object> params);
}