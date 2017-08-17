package com.lty.rt.psgForecast.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.comm.util.DateUtil;
import com.lty.rt.psgForecast.bean.DataPoint;
import com.lty.rt.psgForecast.bean.RegressionLine;
import com.lty.rt.psgForecast.mapper.AreaPsgflowForecastMapper;
import com.lty.rt.psgForecast.util.ForecastUtil;
import com.lty.rt.psgForecast.util.ICallBack;

/**
 * 交通小区客流预测Service
 * 
 * @author Administrator
 * 
 */
@Service
public class AreaPsgFlowForecastService {
	
	@Autowired
	private AreaPsgflowForecastMapper areaPsgflowForecastMapper;
	
	
	public Map<String, Object> forecast(Map<String, Object> params) throws ParseException {

		//通过月份预测
		Map<String, Object> result = forecastMonth(params);
		
		List<Map<String, Object>> areasData = areaPsgflowForecastMapper.queryAreasData(params);
		if(CollectionUtils.isNotEmpty(areasData)){
			String areaId = (String) params.get("areaId");
			for(Map<String, Object> areaData : areasData){
				params.put("areaId", areaData.get("CODEID"));
				if(areasData.size() == 1){
					areaData.put("flowValue", result.get("forecastMonthValue"));
				}else{
					Map<String, Object> temp = forecastMonth(params);
					areaData.put("flowValue", temp.get("forecastMonthValue"));
				}
			}
			params.put("areaId", areaId);
		}

		result.put("areasData", areasData);
		
		//计算每天的数据
		List<Map<String, Object>> daysData =  ForecastUtil.forecastDay(params, new ICallBack(){

			@Override
			public List<Map<String, Object>> call(Map<String, Object> params) {
				// TODO Auto-generated method stub
				return  areaPsgflowForecastMapper.queryDaysData(params);
			}
			
		});
		
		result.put("daysData", daysData);
		return result;
	}
	
	/*public Map<String, Object> forecast(Map<String, Object> params) throws ParseException {

		//通过月份预测
		Map<String, Object> result = forecastMonth(params);
		
		List<Map<String, Object>> areasData = areaPsgflowForecastMapper.queryAreasData(params);
		if(CollectionUtils.isNotEmpty(areasData)){
			String areaId = (String) params.get("areaId");
			for(Map<String, Object> areaData : areasData){
				params.put("areaId", areaData.get("CODEID"));
				if(areasData.size() == 1){
					areaData.put("flowValue", result.get("forecastMonthValue"));
				}else{
					Map<String, Object> temp = forecastMonth(params);
					areaData.put("flowValue", temp.get("forecastMonthValue"));
				}
			}
			params.put("areaId", areaId);
		}

		result.put("areasData", areasData);
		
		//计算每天的数据
		List<Map<String, Object>> daysData = new ArrayList<Map<String, Object>>();
		
		Date yc_month1 = (Date) params.get("yc_month1");
		Date yc_month2 = (Date) params.get("yc_month2");
		Date tmp = yc_month1;
		
		Date begin = (Date)params.get("begin");
		Date end = (Date)params.get("end");
		Date end2 = DateUtil.addDate(DateUtil.addDate(end, "M", 1), "D", -1);

		List<String> forcastMonths = new LinkedList<String>();
		while(DateUtil.compareDate(tmp, yc_month2)<=0){
			forcastMonths.add(DateUtil.convertDateToString(DateUtil.DATE_MOTH, tmp));
			tmp = DateUtil.addDate(tmp, "M", 1);
		}
		
		//是否全部预测
		Boolean type = (Boolean)params.get("type"); 
		int maxDays = (int)params.get("maxdays");
		
		//根据同期数据预测每天的客流量
		if(!type){
			
			for(String fm : forcastMonths){
				for(int i=1; i<=maxDays; i++){
					int  forecastValue = 0;
					List<String> items = new LinkedList<String>();
					String str_date = fm + String.format("-%02d", i);
					
					if(i>28 && !DateUtil.isDatePattern(str_date, DateUtil.DATE)){
						continue;
					}

					Date curDate = DateUtil.convertStringToDate(DateUtil.DATE, str_date);
					int j = -1;
					Date temp = DateUtil.addDate(curDate, "Y", j);
					for(;;){
						if(DateUtil.compareDate(temp, end)>0){
							temp = DateUtil.addDate(temp, "Y", --j);
							continue;
						}
						if(DateUtil.compareDate(temp, begin)<0){
							break;
						}
						items.add(str_date);
					}
					
					if(items.size()==0){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("DAYS", str_date);
						map.put("FORECAST_COUNT", forecastValue);
						map.put("MESSAGE", "该日没有元数据");
						
						daysData.add(map);
						continue;
					}
					
					params.put("items", items);
					
					List<Map<String, Object>> list = areaPsgflowForecastMapper
							.queryDaysData(params);
					
					if(list.size()==0){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("DAYS", str_date);
						map.put("FORECAST_COUNT", forecastValue);
						map.put("MESSAGE", "该日没有元数据");
						
						daysData.add(map);
					}else if(list.size()==1){
						int baseValue = Integer.valueOf(list.get(0).get("TOTAL_PERSON_COUNT").toString()); 
						Random r = new Random();
						Map<String, Object> map = new HashMap<String, Object>();
						int r1 = r.nextInt(10);
						int r2 = 0-r.nextInt(10);
						int r3 = (r1 + r2)/2;
						if(r3==0){
							r3 = r.nextInt(10);
						}
						forecastValue = baseValue * (100 + r3) /100;
						map.put("DAYS", str_date);
						map.put("FORECAST_COUNT", forecastValue);
						daysData.add(map);
					}else{
						RegressionLine line = new RegressionLine();
						for (int j1 = 1; j1 <= list.size(); j1++) {
							Map<String, Object> data = list.get(j1 - 1);
							BigDecimal totalCount = (BigDecimal) data
									.get("TOTAL_PERSON_COUNT");
							line.addDataPoint(new DataPoint(j1, totalCount.floatValue()));
						}
						forecastValue = Math
								.abs((int) (line.getA1() * (list.size() + 1) + line.getA0()));
						
						//数据修正
						if(forecastValue<=0){
							int bv = Integer.valueOf(list.get(list.size()-1).get("TOTAL_PERSON_COUNT").toString());
							forecastValue = bv*80/100;
						}
						
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("DAYS", str_date);
						map.put("FORECAST_COUNT", forecastValue);
						daysData.add(map);
					}
				}
			}
			
			Collections.sort(daysData, new Comparator<Map<String, Object>>(){

				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					// TODO Auto-generated method stub
					String day1 = o1.get("DAYS").toString();
					String day2 = o2.get("DAYS").toString();
					return day1.compareTo(day2);
				}
				
			});
			
			result.put("daysData", daysData);
			return result;
		}
		
		
		for(int i=1; i<=maxDays; i++){
			int  forecastValue = 0;
			List<String> items = new LinkedList<String>();
			
			Date tmp2 = null;
			if(i<=28){
				tmp2 = DateUtil.addDate(begin, "D", i-1);
			}else{
				String str_begin = DateUtil.convertDateToString(DateUtil.DATE, begin);
				String begin29 = str_begin.substring(0, 6)+"-"+i;
				if(DateUtil.isDatePattern(begin29, DateUtil.DATE)){
					tmp2 = DateUtil.addDate(DateUtil.addDate(begin, "M", 1), "D", i-1);
				}else{
					tmp2 = DateUtil.addDate(begin, "D", i-1);
				}
			}
			Date pre = null;
			
			while(DateUtil.compareDate(tmp2, end2)<=0){
				items.add(DateUtil.convertDateToString( DateUtil.DATE , tmp2));
				if(i<=28){
					tmp2 = DateUtil.addDate(tmp2, "M", 1);
					continue;
				}else{
					pre = tmp2;
					tmp2 = DateUtil.addDate(tmp2, "M", 1);
					SimpleDateFormat sdf = new SimpleDateFormat("dd");
					int curDd = Integer.parseInt(sdf.format(tmp2));
					if(curDd!=i){
						tmp2 = DateUtil.addDate(pre, "M", 2);
					}
				}
			}

			if(items.size()==0){
				for(String fm : forcastMonths){
					String str_date = fm + String.format("-%02d", i);
					if(!DateUtil.isDatePattern(str_date, DateUtil.DATE)){
						continue;
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("DAYS", str_date);
					map.put("FORECAST_COUNT", forecastValue);
					map.put("MESSAGE", "该日没有元数据");
					
					daysData.add(map);
				}
				continue;
			}
			
			params.put("items", items);
			
			List<Map<String, Object>> list = areaPsgflowForecastMapper
					.queryDaysData(params);
			
			
			//若是一天数据都没有，预测结果为0， 若是只有一天的数据，就是随机波动, 若是有两天以上的数据,就用一元线程回归进行预测
			if(list.size()==0){
				for(String fm : forcastMonths){
					String str_date = fm + String.format("-%02d", i);
					if(!DateUtil.isDatePattern(str_date, DateUtil.DATE)){
						continue;
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("DAYS", str_date);
					map.put("FORECAST_COUNT", forecastValue);
					map.put("MESSAGE", "该日没有元数据");
					
					daysData.add(map);
				}
			}else if(list.size()==1){
				int baseValue = Integer.valueOf(list.get(0).get("TOTAL_PERSON_COUNT").toString()); 
				for(String fm : forcastMonths){
					String str_date = fm + String.format("-%02d", i);
					if(!DateUtil.isDatePattern(str_date, DateUtil.DATE)){
						continue;
					}
					Random r = new Random();
					Map<String, Object> map = new HashMap<String, Object>();
					int r1 = r.nextInt(10);
					int r2 = 0-r.nextInt(10);
					int r3 = (r1 + r2)/2;
					if(r3==0){
						r3 = r.nextInt(10);
					}
					forecastValue = baseValue * (100 + r3) /100;
					map.put("DAYS", str_date);
					map.put("FORECAST_COUNT", forecastValue);
					daysData.add(map);
				}
			}else if(list.size()>=2){
				RegressionLine line = new RegressionLine();
				for (int j = 1; j <= list.size(); j++) {
					Map<String, Object> data = list.get(j - 1);
					BigDecimal totalCount = (BigDecimal) data
							.get("TOTAL_PERSON_COUNT");
					line.addDataPoint(new DataPoint(j, totalCount.floatValue()));
				}
				int z = 1;
				int bv=0;
				for(String fm : forcastMonths){
					String str_date = fm + String.format("-%02d", i);
					if(!DateUtil.isDatePattern(str_date, DateUtil.DATE)){
						continue;
					}
					forecastValue = Math
							.abs((int) (line.getA1() * (list.size() + (z++)) + line.getA0()));

					if(forecastValue<=0){
						forecastValue = bv*80/100;
					}
					
					bv = forecastValue;
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("DAYS", str_date);
					map.put("FORECAST_COUNT", forecastValue);
					daysData.add(map);
				}
			}
			
			
		}*/
		
		
		// 计算预测月份每天的客流走势
		/*List<Map<String, Object>> daysData = new ArrayList<Map<String, Object>>();
		String[] days = (String[]) params.get("days");
		int prevDateForecastValue = 0;
		int forecastValue = 0;
		for (String day : days) {
			Map<String, Object> map = new HashMap<String, Object>();
			params.put("item", day);
			List<Map<String, Object>> list = areaPsgflowForecastMapper
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
		Collections.sort(daysData, new Comparator<Map<String, Object>>(){

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				// TODO Auto-generated method stub
				String day1 = o1.get("DAYS").toString();
				String day2 = o2.get("DAYS").toString();
				return day1.compareTo(day2);
			}
			
		});
		result.put("daysData", daysData);
		return result;

	}*/

	private Map<String, Object> forecastMonth(Map<String, Object> params) throws ParseException{
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询分析时段月历史客流数据
		List<Map<String, Object>> monthData = areaPsgflowForecastMapper
				.queryMonthData(params);

		return ForecastUtil.forecastMonth(monthData, params);

	}
	
	/*private Map<String, Object> forecastMonth(Map<String, Object> params) throws ParseException{
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询分析时段月历史客流数据
		List<Map<String, Object>> monthData = areaPsgflowForecastMapper
				.queryMonthData(params);
		
		if(monthData.size()<2){
			throw new RuntimeException("预测源数据不足两个月");
		}
		
		List<String> forcastMonths = new LinkedList<String>();
		
		Date yc_month1 = (Date) params.get("yc_month1");
		Date yc_month2 = (Date) params.get("yc_month2");
		Date tmp = yc_month1;
		
		Date begin = (Date)params.get("begin");
		Date end = (Date)params.get("end");

		while(DateUtil.compareDate(tmp, yc_month2)<=0){
			forcastMonths.add(DateUtil.convertDateToString(DateUtil.DATE_MOTH, tmp));
			tmp = DateUtil.addDate(tmp, "M", 1);
		}
		
		Boolean type = (Boolean)params.get("type"); 
		
		// 根据历史数据，推算预测月份总体客流量
		RegressionLine line = new RegressionLine();
		List<Map<String, Object>> forcasts = new LinkedList<Map<String, Object>>();
		
		//处理同期
		if(!type){
			for(String fm : forcastMonths){
				List<Map<String, Object>> fm_monthData = new LinkedList<Map<String, Object>>();
				Date fm_date = DateUtil.addDate(DateUtil.convertStringToDate(DateUtil.DATE_MOTH, fm), "Y", -1);
				while(DateUtil.compareDate(fm_date, begin)>=0){
					String key = DateUtil.convertDateToString(DateUtil.DATE_MOTH, fm_date);
					for(Map<String, Object> month : monthData){
						if(key.equals(month.get("MM").toString())){
							fm_monthData.add(month);
							break;
						}
					}
					fm_date = DateUtil.addDate(fm_date, "Y", -1);
				}
				if(fm_monthData.size()<2){
					throw new RuntimeException("月份["+fm+"]不足两月同期数据");
				}
				
				for (int i = 1; i <= fm_monthData.size(); i++) {
					Map<String, Object> data = fm_monthData.get(i - 1);
					BigDecimal totalCount = (BigDecimal) data.get("TOTAL_PERSON_COUNT");
					line.addDataPoint(new DataPoint(i, totalCount.floatValue()));
				}
				
				//计算结果
				int forecastValue = (int) (line.getA1() * (fm_monthData.size() + 1) + line
						.getA0());
				if(forecastValue < 0){
					forecastValue = 0;
				}
				
				//保存预测月的计算结果
				Map<String, Object> forecastData = new HashMap<String, Object>();
				forecastData.put("MM", fm);
				forecastData.put("TOTAL_PERSON_COUNT", forecastValue);
				forecastData.put("IS_FORECAST", true);
				
				monthData.add(forecastData);
				forcasts.add(forecastData);
			}
			
			result.put("monthData", monthData);
			result.put("forecastMonthValue", forcasts);
			
			return result;
		}
		
		//处理全部
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
		int bv = 0;
		for(int i=0; i<forcastMonths.size(); i++){
			forecastValue = (int) (line.getA1() * (monthData.size() + 1+i) + line
					.getA0());
			
			if(forecastValue<=0){
				forecastValue = bv*80/100;
			}
			bv = forecastValue;
			
			Map<String, Object> forecastData = new HashMap<String, Object>();
			forecastData.put("MM", forcastMonths.get(i));
			forecastData.put("TOTAL_PERSON_COUNT", forecastValue);
			forecastData.put("IS_FORECAST", true);

			monthData.add(forecastData);
			forcasts.add(forecastData);
		}
		

		// 预测结果加入返回List里面
		Map<String, Object> forecastData = new HashMap<String, Object>();
		forecastData.put("MM", "2100-10");
		forecastData.put("TOTAL_PERSON_COUNT", forecastValue);
		monthData.add(forecastData);
		Collections.sort(monthData, new Comparator<Map<String, Object>>(){

			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				// TODO Auto-generated method stub
				String day1 = o1.get("MM").toString();
				String day2 = o2.get("MM").toString();
				return day1.compareTo(day2);
			}
			
		});
		result.put("monthData", monthData);
		//result.put("forecastMonthValue", forecastValue);
		result.put("forecastMonthValue", forcasts);
		
		return result;
	}*/
	
	/*public List<Map<String, Object>> forecastOneDayData(
			Map<String, Object> params) {
		List<Map<String, Object>> areasData = new LinkedList<Map<String, Object>>();
		
		//小区Id
		String areaId = (String) params.get("areaId");
		
		//根据是否查找全部
		if("-1".equals(areaId)){
			params.put("areaId", "");
			areasData = areaPsgflowForecastMapper.queryAreasData(params);
			for(Map<String, Object> areaLon : areasData){
				params.put("areaId", areaLon.get("CODEID"));
				List<Map<String, Object>> list = areaPsgflowForecastMapper
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
			areasData = areaPsgflowForecastMapper.queryAreasData(params);
		}
		
		return areasData;
	}*/
	
	
	public List<Map<String, Object>> forecastOneDayData(
			Map<String, Object> params) {
		List<Map<String, Object>> areasData = new LinkedList<Map<String, Object>>();
		
		//小区Id
		String areaId = (String) params.get("areaId");
		
		if("-1".equals(areaId)){
			params.put("areaId", "");
		}
		areasData = areaPsgflowForecastMapper.queryAreasData(params);
		if(areasData.size()<=1){
			return areasData;
		}
		
		for(Map<String, Object> areaLon : areasData){
			params.put("areaId", areaLon.get("CODEID"));
			List<Map<String, Object>> list = areaPsgflowForecastMapper
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
		
		return areasData;
	}
}
