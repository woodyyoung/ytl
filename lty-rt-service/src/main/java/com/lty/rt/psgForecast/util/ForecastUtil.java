package com.lty.rt.psgForecast.util;

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

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import com.lty.rt.comm.util.DateUtil;
import com.lty.rt.psgForecast.bean.DataPoint;
import com.lty.rt.psgForecast.bean.RegressionLine;

/**
 * 参数校验，月度预测，天预测
 * @author thy 20170418
 *
 */
public class ForecastUtil {
	

	/**
	 * 
	 * @param map html提交的参数，做参数规则校验
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> validata(Map<String, Object> map) throws ParseException{
		Assert.notNull(map, "参数不能为空");
		
		//校验是否有同期，全部类型的区分, 默认是全部
		Object str_type = map.get("type");
		Assert.notNull(str_type, "必须指定预测类型");
		Boolean type = "0".equals(str_type.toString())?true:false;
		map.put("type", type);
		
		//校验是否有预测月份,结束月必须大于等于开始月
		List<String> yc_months = (List<String>) map.get("yc_month");
		if(yc_months==null || yc_months.size()==0){
			throw new IllegalArgumentException("缺少预测月份");
		}
		String yc_month1 = yc_months.get(0);
		String yc_month2 = null;
		if(yc_months.size()==1){
			yc_month2 = yc_month1;
		}else{
			yc_month2 = yc_months.get(1);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date firstDayOfMonth = sdf.parse(yc_month1);
		Date firstDayOfMonth2 = sdf.parse(yc_month2);
		if(DateUtil.compareDate(firstDayOfMonth, firstDayOfMonth2)>0){
			throw new IllegalArgumentException("预测结束月必须大于开始月份");
		}
		map.put("yc_month1", firstDayOfMonth);
		map.put("yc_month2", firstDayOfMonth2);
		
		//校验是否有两个月以上的数据
		Assert.notNull(map.get("beginDate"), "缺少源数据");
		Assert.notNull(map.get("endDate"), "缺少源数据");
		Date begin = sdf.parse(map.get("beginDate").toString());
		Date end = sdf.parse(map.get("endDate").toString());
		Date beginIncr2 = DateUtil.addDate(begin, "M", 1);
		if(DateUtil.compareDate(begin, end)>=0 || DateUtil.compareDate(beginIncr2, end)>0){
			throw new IllegalArgumentException("来源数据最起码要包含两个月的数据");
		}
		map.put("begin", begin);
		map.put("end", end);
		
		if(DateUtil.compareDate(firstDayOfMonth, end)<=0){
			throw new IllegalArgumentException("预测与来源重合");
		}
		
		//计算预测月份里面最大的月份天数
		int maxdays = 0;
		Date beginInc1 = begin;
		while(DateUtil.compareDate(beginInc1, end)<=0){
			Date endDayOfMonth = DateUtils.addDays(
					DateUtils.addMonths(beginInc1, 1), -1);
			sdf = new SimpleDateFormat("dd");
			int endDayIndex = Integer.parseInt(sdf.format(endDayOfMonth));
			if(endDayIndex>maxdays){
				maxdays = endDayIndex;
			}
			beginInc1 = DateUtil.addDate(beginInc1, "M", 1);
		}
		map.put("maxdays", maxdays);
		
		return map;
	}

	
	/**
	 *  月度校验
	 * @param monthData  数据库中保存的月数据
	 * @param params  html提交的参数
	 * @return   预测结果
	 * @throws ParseException
	 */
	public static Map<String, Object> forecastMonth(List<Map<String, Object>> monthData, Map<String, Object> params) throws ParseException{
		
		Map<String, Object> result = new HashMap<String, Object>();
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
				
				List<Integer> src = new ArrayList<Integer>(fm_monthData.size());
				for (int i = 1; i <= fm_monthData.size(); i++) {
					Map<String, Object> data = fm_monthData.get(i - 1);
					BigDecimal totalCount = (BigDecimal) data.get("TOTAL_PERSON_COUNT");
					src.add(totalCount.intValue());
				}
				
				// 预测结果
				List<Integer> dest = forecast(src, 1);
				
				//保存预测月的计算结果
				Map<String, Object> forecastData = new HashMap<String, Object>();
				forecastData.put("MM", fm);
				forecastData.put("TOTAL_PERSON_COUNT", dest.get(0));
				forecastData.put("IS_FORECAST", true);
				
				monthData.add(forecastData);
				forcasts.add(forecastData);
			}
			
			result.put("monthData", monthData);
			result.put("forecastMonthValue", forcasts);
			
			return result;
		}else{
		    //依据全部日期来进行预测
			
			//部分单个预测对象没有数据，跳过不处理
			if(monthData.size()==0){
				return result;
			}
			
			List<Integer> src = new ArrayList<Integer>(monthData.size());
			for (int i = 1; i <= monthData.size(); i++) {
				Map<String, Object> data = monthData.get(i - 1);
				BigDecimal totalCount = (BigDecimal) data.get("TOTAL_PERSON_COUNT");
				src.add(totalCount.intValue());
				line.addDataPoint(new DataPoint(i, totalCount.floatValue()));
			}
			
			
			// 预测结果
			List<Integer> dest = forecast(src, forcastMonths.size());
			
			for(int i=0; i<forcastMonths.size(); i++){
				Map<String, Object> forecastData = new HashMap<String, Object>();
				forecastData.put("MM", forcastMonths.get(i));
				forecastData.put("TOTAL_PERSON_COUNT", dest.get(i));
				forecastData.put("IS_FORECAST", true);
	
				monthData.add(forecastData);
				forcasts.add(forecastData);
			}
	
			// 预测结果加入返回List里面
			result.put("monthData", monthData);
			result.put("forecastMonthValue", forcasts);
			
			return result;
		}
		
	}

	
	/**
	 * 具体的预测算法
	 *  src.size();若是size()等于1,则随机上下浮动{wave},目前是10%(和项目经理确认过);若是size()大于1,则根据一元线性回归来进行预测
	 * @param fm_monthData 进行预测的源数据
	 * @param length 预测多少个月
	 * @return
	 */
	public static List<Integer> forecast(List<Integer> src, int length){
		
		Assert.notNull(src, "缺少源数据");
		Assert.isTrue(src.size()>0, "最少要有一个月的数据");
		Assert.isTrue(length>0, "预测周期最小为1");
		
		int wave = 10 ;    //上下浮动系数
		int forecastValue = 0;
		
		List<Integer> dest = new ArrayList<Integer>(length);
		
		if(src.size()==1){
			int baseValue = src.get(0); 
			int i=0;
			while((i++)<length){
				Random r = new Random();
				int r1 = r.nextInt(wave*2);
				int r2 = -r.nextInt(wave*2);
				int r3 = r2+r1;
				if(r3 > wave){
					r3 = r3 - wave;
				}
				if(r3 < -wave){
					r3 = r3 + wave;
				}
				if(r3==0){
					r3 = r.nextInt(wave);
				}
				forecastValue = baseValue * (100 + r3) /100;
				dest.add(forecastValue);
			}
		}else{
			RegressionLine line = new RegressionLine();
			for (int j1 = 1; j1 <= src.size(); j1++) {
				line.addDataPoint(new DataPoint(j1, src.get(j1-1)));
			}
			int i=0;
			while((i++)<length){
				forecastValue = (int) (line.getA1() * (src.size() + i) + line.getA0());
				
				//项目经理要求做数据修正，若是为0,就取前面的预测的数据波动20%
				if(forecastValue<=0){
					if(i==1){
						forecastValue = src.get(src.size()-1)*80/100;
					}else{
						forecastValue = dest.get(i-2)*80/100;
					}
				}
				
				dest.add(forecastValue);
			}
		}
		
		return dest;
	}

	/**
	 * @param begin 源日期的开始天
	 * @param end  源日期的结束天
	 * @param month  同期所在月份
	 * @param day   日期
	 * @param isPeriod  是否同期
	 * @return   符合month-day-isPeriod组合的所有日期
	 * @throws ParseException
	 */
	public static List<String> daysOfString(Date begin, Date end, String month, int day, boolean isPeriod ) throws ParseException{
		
		List<String> items = new LinkedList<String>();

		if(isPeriod){
			//同期，逆向推测.
			Assert.notNull(month, "同期必须指定月份");
			String str_date = month + String.format("-%02d", day);
			if(day>28 && !DateUtil.isDatePattern(str_date, DateUtil.DATE)){
				return null;
			}
			int j = 0;
			Date curDate = DateUtil.convertStringToDate(DateUtil.DATE, str_date);
			boolean is0229 = "02-29".equals(str_date.substring(5));   //若是02月29号的话,再倒推的时候可能连续出现不成立的情况
			Date temp = curDate;
			for(;;){
				temp = DateUtil.addDate(curDate, "Y", --j);
				if(DateUtil.compareDate(temp, end)>0){
					continue;
				}
				if(is0229){
					SimpleDateFormat sdf = new SimpleDateFormat("dd");
					int curDd = Integer.parseInt(sdf.format(temp));
					while(curDd!=29){
						temp = DateUtil.addDate(curDate, "Y", --j);
						curDd = Integer.parseInt(sdf.format(temp));
					}
				}
				if(DateUtil.compareDate(temp, begin)<0){
					break;
				}
				items.add(DateUtil.convertDateToString(DateUtil.DATE, temp));
			}
		}else{
			//环比的话,直接从begin开始顺向推测
			Date tmp2 = null;
			if(day<=28){
				tmp2 = DateUtil.addDate(begin, "D", day-1);
			}else{
				String str_begin = DateUtil.convertDateToString(DateUtil.DATE, begin);
				String begin29 = str_begin.substring(0, 6)+"-"+day;
				if(DateUtil.isDatePattern(begin29, DateUtil.DATE)){
					//例如2月没有30号,此时若begin是从2月份开始的,直接跳到下一个月开始。
					//不会连续两个月出现不满足条件的起始日
					tmp2 = DateUtil.addDate(DateUtil.addDate(begin, "M", 1), "D", day-1);
				}else{
					tmp2 = DateUtil.addDate(begin, "D", day-1);
				}
			}
			
			Date pre = null;
			
			while(DateUtil.compareDate(tmp2, end)<=0){
				items.add(DateUtil.convertDateToString( DateUtil.DATE , tmp2));
				if(day<=28){
					tmp2 = DateUtil.addDate(tmp2, "M", 1);
					continue;
				}else{
					pre = tmp2;
					tmp2 = DateUtil.addDate(tmp2, "M", 1);
					SimpleDateFormat sdf = new SimpleDateFormat("dd");
					//下面两行若是下个月的日期和要求的不一样，直接跳到下下个月
					//不会连续两个月出现不满足条件的起始日
					int curDd = Integer.parseInt(sdf.format(tmp2));
					if(curDd!=day){
						tmp2 = DateUtil.addDate(pre, "M", 2);
					}
				}
			}
		}
		
		return items;
	}
	
	/**
	 * 计算每天的数据
	 * @param params
	 * @return
	 * @throws ParseException 
	 */
	public static List<Map<String, Object>> forecastDay(Map<String, Object> params, ICallBack cb) throws ParseException{

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
		
		if(!type){
		//同期预测,循环依据是月先
			for(String fm : forcastMonths){
				for(int i=1; i<=maxDays; i++){
					int  forecastValue = 0;
					String str_date = fm + String.format("-%02d", i);
					
					//获取日期
					List<String> items = daysOfString(begin, end2, fm, i, true);
	
					if(items==null || items.size()==0){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("DAYS", str_date);
						map.put("FORECAST_COUNT", forecastValue);
						map.put("MESSAGE", "该日没有元数据");
						
						daysData.add(map);
						continue;
					}
					
					params.put("items", items);
					
					List<Map<String, Object>> list = cb.call(params);
					
					//进行预测
					
					//校验
					if(list.size()==0){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("DAYS", str_date);
						map.put("FORECAST_COUNT", forecastValue);
						map.put("MESSAGE", "该日没有元数据");
						
						daysData.add(map);
						continue;
					}
					
					//预测
					List<Integer> src = new ArrayList<Integer>(list.size());
					for(Map<String, Object> row : list){
						int total = Integer.valueOf(row.get("TOTAL_PERSON_COUNT").toString()); 
						src.add(total);
					}
					List<Integer> des = forecast(src, 1);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("DAYS", str_date);
					map.put("FORECAST_COUNT", des.get(0));
					
					daysData.add(map);
				}
			}
		}else{
			//环比预测,不用考虑月
			for(int i=1; i<=maxDays; i++){
				int  forecastValue = 0;
				List<String> items = daysOfString(begin, end2, null, i, false);

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
				
				List<Map<String, Object>> list = cb.call(params);
				
				
				//若是一天数据都没有，预测结果为0
				if(list.size()==0){
					for(String fm : forcastMonths){
						String str_date = fm + String.format("-%02d", i);
						if(!DateUtil.isDatePattern(str_date, DateUtil.DATE)){
							continue;
						}
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("DAYS", str_date);
						map.put("FORECAST_COUNT", forecastValue);
						map.put("MESSAGE", "该日没有源数据");
						
						daysData.add(map);
					}
					continue;
				}
				
				List<Integer> src = new ArrayList<Integer>(list.size());
				for(Map<String, Object> row : list){
					int total = Integer.valueOf(row.get("TOTAL_PERSON_COUNT").toString()); 
					src.add(total);
				}

				//提取出需要进行预测的日期
				List<String> forcastMonths2 = new LinkedList<String>();
				for(String fm : forcastMonths){
					String str_date = fm + String.format("-%02d", i);
					if(!DateUtil.isDatePattern(str_date, DateUtil.DATE)){
						continue;
					}
					forcastMonths2.add(str_date);
				}
				if(forcastMonths2.size()==0){
					continue;
				}
				List<Integer> des = forecast(src, forcastMonths2.size());
				int j = 0;
				for(String str_date : forcastMonths2){
					forecastValue = des.get(j++);
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
		
		
		return daysData;
	}
}
