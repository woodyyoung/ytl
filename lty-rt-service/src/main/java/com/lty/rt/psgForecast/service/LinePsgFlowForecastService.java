package com.lty.rt.psgForecast.service;

import java.math.BigDecimal;
import java.sql.Clob;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.basicData.bean.Line;
import com.lty.rt.basicData.service.LineService;
import com.lty.rt.psgForecast.bean.DataPoint;
import com.lty.rt.psgForecast.bean.RegressionLine;
import com.lty.rt.psgForecast.mapper.LinePsgflowForecastMapper;
import com.lty.rt.psgForecast.util.ForecastUtil;
import com.lty.rt.psgForecast.util.ICallBack;
import com.lty.rt.utils.DataTransfer;

/**
 * 线路客流预测Service
 * 
 * @author Administrator
 * 
 */
@Service
public class LinePsgFlowForecastService {
	@Autowired
	private LinePsgflowForecastMapper linePsgflowForecastMapper;
	
	@Autowired
	private LineService lineService;

	/*public Map<String, Object> forecast(Map<String, Object> params) throws ParseException {
		
		Map<String, Object> result = forecastMonth(params);
		
		if(StringUtils.isBlank((String)params.get("lineId"))){
			List<Line> lines = lineService.listAllLine();
			if(CollectionUtils.isNotEmpty(lines)){
				List<Map<String, Object>> linesData = new LinkedList<Map<String, Object>>();
				for(Line line : lines){
					Map<String, Object> lineData = new HashMap<String, Object>();
					params.put("lineId", line.getId());
					Map<String, Object> temp = forecastMonth(params);
					lineData.put("lineId", line.getId());
					lineData.put("lineName", line.getName());
					lineData.put("flowValue", temp.get("forecastMonthValue"));
					linesData.add(lineData);
				}
				params.put("lineId", "");
				result.put("linesData", linesData);
			}
		}else{
			List<Map<String, Object>> linesData = new LinkedList<Map<String, Object>>();
			Line line = lineService.selectByPrimaryKey((String)params.get("lineId"));
			Map<String, Object> lineData = new HashMap<String, Object>();
			lineData.put("lineId", line.getId());
			lineData.put("flowValue", result.get("forecastMonthValue"));
			linesData.add(lineData);
			result.put("linesData", linesData);
		}
		
		// 计算预测月份每天的客流走势
		List<Map<String, Object>> daysData = new ArrayList<Map<String, Object>>();
		String[] days = (String[]) params.get("days");
		int prevDateForecastValue = 0;
		int forecastValue = 0;
		for (String day : days) {
			Map<String, Object> map = new HashMap<String, Object>();
			params.put("item", day);
			List<Map<String, Object>> list = linePsgflowForecastMapper
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
		}
		result.put("daysData", daysData);
		
		//查询对应的线路信息
		if(StringUtils.isNoneBlank((String)params.get("lineId"))){
			Line sline = lineService.selectByPrimaryKey((String)params.get("lineId"));
			String lineData = DataTransfer.clobToString((Clob) sline.getLpath());
			result.put("lineData", lineData);
		}else{
			Map<String, String> lineDataMap = new HashMap<String, String>();
			List<Line> lineList = lineService.listAllLineDetail();
			if(CollectionUtils.isNotEmpty(lineList)){
				for(Line li : lineList){
					String lineData = DataTransfer.clobToString((Clob) li.getLpath());
					lineDataMap.put(li.getId(), lineData);
				}
				result.put("lineDataMap", lineDataMap);
			}
		}
		
		return result;
	}*/
	
	
	public Map<String, Object> forecast(Map<String, Object> params) throws ParseException {
		
		Map<String, Object> result = forecastMonth(params);
		
		if(StringUtils.isBlank((String)params.get("lineId"))){
			List<Line> lines = lineService.listAllLine();
			if(CollectionUtils.isNotEmpty(lines)){
				List<Map<String, Object>> linesData = new LinkedList<Map<String, Object>>();
				for(Line line : lines){
					Map<String, Object> lineData = new HashMap<String, Object>();
					params.put("lineId", line.getId());
					Map<String, Object> temp = forecastMonth(params);
					lineData.put("lineId", line.getId());
					lineData.put("lineName", line.getName());
					lineData.put("flowValue", temp.get("forecastMonthValue"));
					linesData.add(lineData);
				}
				params.put("lineId", "");
				result.put("linesData", linesData);
			}
		}else{
			List<Map<String, Object>> linesData = new LinkedList<Map<String, Object>>();
			Line line = lineService.selectByPrimaryKey((String)params.get("lineId"));
			Map<String, Object> lineData = new HashMap<String, Object>();
			lineData.put("lineId", line.getId());
			lineData.put("flowValue", result.get("forecastMonthValue"));
			linesData.add(lineData);
			result.put("linesData", linesData);
		}
		
		// 计算预测月份每天的客流走势
		List<Map<String, Object>> daysData = ForecastUtil.forecastDay(params, new ICallBack(){

			@Override
			public List<Map<String, Object>> call(Map<String, Object> params) {
				// TODO Auto-generated method stub
				return linePsgflowForecastMapper.queryDaysData(params);
			}
			
		});
		result.put("daysData", daysData);
		
		//查询对应的线路信息
		if(StringUtils.isNoneBlank((String)params.get("lineId"))){
			Line sline = lineService.selectByPrimaryKey((String)params.get("lineId"));
			String lineData = DataTransfer.clobToString((Clob) sline.getLpath());
			result.put("lineData", lineData);
		}else{
			Map<String, String> lineDataMap = new HashMap<String, String>();
			List<Line> lineList = lineService.listAllLineDetail();
			if(CollectionUtils.isNotEmpty(lineList)){
				for(Line li : lineList){
					String lineData = DataTransfer.clobToString((Clob) li.getLpath());
					lineDataMap.put(li.getId(), lineData);
				}
				result.put("lineDataMap", lineDataMap);
			}
		}
		
		return result;
	}
	
	
	/**
	 * 预测月份
	 * @param params
	 * @return
	 * @throws ParseException 
	 */
	private Map<String, Object> forecastMonth(Map<String, Object> params) throws ParseException{
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询分析时段月历史客流数据
		List<Map<String, Object>> monthData = linePsgflowForecastMapper
				.queryMonthData(params);
		
		return ForecastUtil.forecastMonth(monthData, params);

		/*// 根据历史数据，推算预测月份总体客流量
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
		return result;*/
	}

	public List<Map<String, Object>> forecastOneDayData(
			Map<String, Object> params) {
		List<Map<String, Object>> linesData = new LinkedList<Map<String, Object>>();
		
		//处理时间
		/*String time = (String) params.get("item");
		params.put("item", ""+time.substring(8));
		params.put("yc_month", time.substring(0, time.length()-3));*/
		
		List<Line> lineList = lineService.listAllLineDetail();
		if(CollectionUtils.isNotEmpty(lineList)){
			for(Line line : lineList){
				Map<String, Object> lineDate = new HashMap<String, Object>();
				lineDate.put("lineId", line.getId());
				lineDate.put("lineName", line.getName());
				params.put("lineId", line.getId());
				List<Map<String, Object>> list = linePsgflowForecastMapper
						.queryDaysData(params);
				
				int forecastValue = 0;
				
				if (list.isEmpty()) {
					lineDate.put("value", forecastValue);
					linesData.add(lineDate);
					continue;
				}
				
				if (list.size() == 1) {
					forecastValue = ((BigDecimal) list.get(0).get(
							"TOTAL_PERSON_COUNT")).intValue();
					forecastValue = (int) (forecastValue + forecastValue * 0.1);
					lineDate.put("value", forecastValue);
					linesData.add(lineDate);
					continue;
				}
				
				RegressionLine calLine = new RegressionLine();
				for (int i = 1; i <= list.size(); i++) {
					Map<String, Object> data = list.get(i - 1);
					BigDecimal totalCount = (BigDecimal) data
							.get("TOTAL_PERSON_COUNT");
					calLine.addDataPoint(new DataPoint(i, totalCount.floatValue()));
				}
				forecastValue = Math
						.abs((int) (calLine.getA1() * (list.size() + 1) + calLine.getA0()));
				lineDate.put("value", forecastValue);
				linesData.add(lineDate);
			}
		}
		return linesData;
	}
}
