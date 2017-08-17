package com.lty.rt.psgForecast.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.comm.util.DateUtil;
import com.lty.rt.psgForecast.service.AreaPsgFlowForecastService;

/**
 * 交通小区客流分析统计 Controller
 * 
 * @author Administrator
 * 
 */
@RequestMapping("/areaForecast")
@Controller
public class AreaPsgFlowForecastController {
	@Autowired
	private AreaPsgFlowForecastService areaPsgFlowForecastService;

	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping("/forecast")
	@ResponseBody
	public RTResponse forecast(@RequestBody Map<String, Object> map)
			throws ParseException {
		RTResponse res = null;
		try{
			String platformID = (String) map.get("areaId");
			if("-1".equals(platformID)){
				map.put("areaId", "");
			}
			
			//校验是否有同期，全部类型的区分, 默认是全部
			Object str_type = map.get("type");
			Assert.notNull(str_type, "必须指定预测类型");
			Boolean type = "0".equals(str_type.toString())?true:false;
			map.put("type", type);
			
			//校验是否有预测月份,结束月必须大于等于开始月
			//String yc_month = (String) map.get("yc_month");
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
			
		/*	Date endDayOfMonth = DateUtils.addDays(
					DateUtils.addMonths(firstDayOfMonth2, 1), -1);
			sdf = new SimpleDateFormat("dd");
			int endDayIndex = Integer.parseInt(sdf.format(endDayOfMonth));
			String[] days = new String[endDayIndex];
			for (int i = 1; i <= endDayIndex; i++) {
				if (i < 10) {
					days[i - 1] = "0" + i;
				} else {
					days[i - 1] = "" + i;
				}
			}*/
			
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
			
			Map<String, Object> data = areaPsgFlowForecastService.forecast(map);
			res = new RTResponse();
			res.setResCode(200);
			res.setData(data);
		}catch(Throwable ex){
			res = new RTResponse();
			res.setResCode(300);
			res.setMsg(ex.getMessage());
		}
		return res;
	}

	@RequestMapping("/forecastOneDayData")
	@ResponseBody
	public RTResponse forecastOneDayData(@RequestBody Map<String, Object> map) {
		RTResponse res = new RTResponse();
		List<Map<String, Object>> data = areaPsgFlowForecastService
				.forecastOneDayData(map);
		res.setData(data);
		return res;
	}
	
	private boolean checkContainer(Date begin, Date end, Date yc1, Date yc2){
		
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(yc1);
		Date tmpDate = calendar.getTime();
		
		Date tmpDate1 = DateUtil.addDate(tmpDate, "Y", -1);
		Date tmpDate2 = DateUtil.addDate(tmpDate, "Y", -2);
		while(DateUtil.compareDate(tmpDate, yc2)<=0){
			if(DateUtil.compareDate(tmpDate1, begin)<0 || DateUtil.compareDate(tmpDate2, end)>0){
				return false;
			}
			tmpDate = DateUtil.addDate(tmpDate, "M", 1);
		}
		
		return true;
	}
	

}
