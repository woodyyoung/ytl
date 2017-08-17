package com.lty.rt.psgForecast.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.psgForecast.bean.DataPoint;
import com.lty.rt.psgForecast.bean.RegressionLine;
import com.lty.rt.psgForecast.mapper.StretchPsgflowForecastMapper;
import com.lty.rt.psgForecast.util.ForecastUtil;
import com.lty.rt.psgForecast.util.ICallBack;

/**
 * 路段断面客流预测Service
 * 
 * @author Administrator
 * 
 */
@Service
public class StretchPsgFlowForecastService {
	@Autowired
	private StretchPsgflowForecastMapper stretchPsgflowForecastMapper;

	public Map<String, Object> forecast(Map<String, Object> params) throws ParseException {
		Map<String, Object> result = forecastMonth(params);

		List<Map<String, Object>> stretchsData = stretchPsgflowForecastMapper.queryStretchsData(params);
		if(CollectionUtils.isNotEmpty(stretchsData)){
			for(Map<String, Object> stretchData : stretchsData){
				params.put("stretchId", stretchData.get("LINEID"));
				if(stretchsData.size() == 1){
					stretchData.put("flowValue", result.get("forecastMonthValue"));
				}else{
					Map<String, Object> temp = forecastMonth(params);
					stretchData.put("flowValue", temp.get("forecastMonthValue"));
					params.put("stretchId", "");
				}
			}
		}
		result.put("stretchsData", stretchsData);
		
		// 计算预测月份每天的客流走势
		
		List<Map<String, Object>> daysData =  ForecastUtil.forecastDay(params, new ICallBack(){

			@Override
			public List<Map<String, Object>> call(Map<String, Object> params) {
				// TODO Auto-generated method stub
				return  stretchPsgflowForecastMapper.queryDaysData(params);
			}
			
		});
		
		/*List<Map<String, Object>> daysData = new ArrayList<Map<String, Object>>();
		String[] days = (String[]) params.get("days");
		int prevDateForecastValue = 0;
		int forecastValue = 0;
		for (String day : days) {
			Map<String, Object> map = new HashMap<String, Object>();
			params.put("item", day);
			List<Map<String, Object>> list = stretchPsgflowForecastMapper
					.queryDaysData(params);
			if (list.isEmpty()) {
				forecastValue = (int) (prevDateForecastValue + prevDateForecastValue * 0.1);
				map.put("DAYS", day);
				map.put("FORECAST_COUNT", forecastValue);
				prevDateForecastValue = forecastValue;
				daysData.add(map);
				continue;
			}
			if (list.size() == 1) {
				forecastValue = ((BigDecimal) list.get(0).get(
						"TOTAL_PERSON_COUNT")).intValue();
				forecastValue = (int) (forecastValue + forecastValue * 0.1);
				map.put("DAYS", day);
				map.put("FORECAST_COUNT", forecastValue);
				prevDateForecastValue = forecastValue;
				daysData.add(map);
				continue;
			}
			RegressionLine line = new RegressionLine();
			for (int i = 1; i <= list.size(); i++) {
				Map<String, Object> data = list.get(i - 1);
				BigDecimal totalCount = (BigDecimal) data
						.get("TOTAL_PERSON_COUNT");
				line.addDataPoint(new DataPoint(i, totalCount.floatValue()));
			}
			forecastValue = Math
					.abs((int) (line.getA1() * (list.size() + 1) + line.getA0()));

			map.put("DAYS", day);
			map.put("FORECAST_COUNT", forecastValue);
			prevDateForecastValue = forecastValue;
			daysData.add(map);
		}*/
		result.put("daysData", daysData);
		return result;
	}

	/**
	 * 预测月份
	 * @param params
	 * @return
	 */
	private Map<String, Object> forecastMonth(Map<String, Object> params) throws ParseException{
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询分析时段月历史客流数据
		List<Map<String, Object>> monthData = stretchPsgflowForecastMapper
				.queryMonthData(params);

		return ForecastUtil.forecastMonth(monthData, params);

	}
	
	/*private Map<String, Object> forecastMonth(Map<String, Object> params){
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询分析时段月历史客流数据
		List<Map<String, Object>> monthData = stretchPsgflowForecastMapper
				.queryMonthData(params);

		// 根据历史数据，推算预测月份总体客流量
		RegressionLine line = new RegressionLine();

		for (int i = 1; i <= monthData.size(); i++) {
			Map<String, Object> data = monthData.get(i - 1);
			BigDecimal totalCount = (BigDecimal) data.get("TOTAL_PERSON_COUNT");
			line.addDataPoint(new DataPoint(i, totalCount.floatValue()));
		}
		// 预测结果
		int forecastValue = 0;
		if (line.getDataPointCount() == 1) {
			forecastValue = ((BigDecimal) monthData.get(0).get(
					"TOTAL_PERSON_COUNT")).intValue();
		} else {
			forecastValue = (int) (line.getA1() * (monthData.size() + 1) + line
					.getA0());
		}

		if(forecastValue < 0){
			forecastValue = 0;
		}
		
		// 预测结果加入返回List里面
		Map<String, Object> forecastData = new HashMap<String, Object>();
		forecastData.put("MM", "2100-10");
		forecastData.put("TOTAL_PERSON_COUNT", forecastValue);
		monthData.add(forecastData);
		result.put("monthData", monthData);
		result.put("forecastMonthValue", forecastValue);
		return result;
	}*/
	
	public List<Map<String, Object>> forecastOneDayData(
			Map<String, Object> params) {
		List<Map<String, Object>> stretchsData = new LinkedList<Map<String, Object>>();
		
		//小区Id
		String stretchId = (String) params.get("stretchId");
		
		//根据是否查找全部
		if("-1".equals(stretchId)){
			params.put("stretchId", "");
			stretchsData = stretchPsgflowForecastMapper.queryStretchsData(params);
			for(Map<String, Object> areaLon : stretchsData){
				params.put("stretchId", areaLon.get("LINEID"));
				List<Map<String, Object>> list = stretchPsgflowForecastMapper
						.queryDaysData(params);
				int forecastValue = 0;
				if (list.isEmpty()) {
					areaLon.put("flowData", forecastValue);
					continue;
				}
				if (list.size() == 1) {
					forecastValue = ((BigDecimal) list.get(0).get(
							"TOTAL_PERSON_COUNT")).intValue();
					forecastValue = (int) (forecastValue + forecastValue * 0.1);
					areaLon.put("flowData", forecastValue);
					continue;
				}
				
				RegressionLine line = new RegressionLine();
				for (int i = 1; i <= list.size(); i++) {
					Map<String, Object> data = list.get(i - 1);
					BigDecimal totalCount = (BigDecimal) data
							.get("TOTAL_PERSON_COUNT");
					line.addDataPoint(new DataPoint(i, totalCount.floatValue()));
				}
				forecastValue = Math
						.abs((int) (line.getA1() * (list.size() + 1) + line.getA0()));
				areaLon.put("flowData", forecastValue);
			}
		}else{
			stretchsData = stretchPsgflowForecastMapper.queryStretchsData(params);
		}
		
		return stretchsData;
	}
}
