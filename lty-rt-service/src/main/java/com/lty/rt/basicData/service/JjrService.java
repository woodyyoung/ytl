package com.lty.rt.basicData.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.lty.rt.basicData.mapper.JjrMapper;
import com.lty.rt.comm.util.DateUtil;

@Service
@Transactional
public class JjrService {
	
	@Autowired(required=true)
	private JjrMapper jjrMapper;
	
	public void flush(){
		try{
			jjrMapper.flushStatus();
		}catch(Throwable ex){
			ex.printStackTrace();
			throw new RuntimeException("更新失败");
		}
	}
	
	public Map<String, List<String>> selectBymonth(String month){
		Assert.hasText(month, "月份不能为空");
		
		List<Map<String, Object>> days = jjrMapper.selectByMonth(month);
		Map<String, List<String>> catalogs = new HashMap<String, List<String>>();
		
		for(Map<String, Object> day : days){
			Object isWork = day.get("FISWORK");
			if(isWork==null){
				throw new RuntimeException("日期属性未知");
			}
			String key = isWork.toString();
			List<String> catalog = catalogs.get(key);
			if(catalog == null){
				catalog = new LinkedList<String>();
				catalogs.put(key, catalog);
			}
			Date date = (Date)(day.get("FDAY"));
			String formmatDay = DateUtil.convertDateToString(DateUtil.DATE, date);
			catalog.add(formmatDay);
		}
		
		return catalogs;
	}

	public void changeState(Map<String, List<Date>> states2, String userName) {
		// TODO Auto-generated method stub
		Assert.notNull(states2, "参数不能为空");
		Assert.hasText(userName, "用户名不能为空");
		
		Set<String> keys = states2.keySet();

		if(keys.size()>1){
			Collection<List<Date>> values0 = states2.values();
			List<List<Date>> values = new ArrayList<List<Date>>(values0.size());
			Iterator<List<Date>> it = values0.iterator();
			while(it.hasNext()){
				values.add(it.next());
			}
			
			for(int i=0; i<values.size()-1; i++){
				for(int j=i+1; j<=values.size()-1; j++){
					List<Date> l1 = new ArrayList<Date>(values.get(i));
					List<Date> l2 = new ArrayList<Date>(values.get(j));
					l1.retainAll(l2);
					if(l1.size()>0){
						throw new RuntimeException(DateUtil.convertDateToString(DateUtil.DATE, l1.get(0))+"时间重复定义");
					}
				}
			}
		}
		
		for(String state : keys){
			List<Date> dates = states2.get(state);
			if(dates.size()==0){
				continue;
			}
			jjrMapper.updateDayState(Integer.valueOf(state), dates, userName);
		}
	}

	public  List<Map<String, String>> selectAll(Date begin, Date end) {
		// TODO Auto-generated method stub
		if(end!=null){
			end = DateUtil.addDate(end, "D", 1);
		}
		List<Map<String, Object>> rows0 = jjrMapper.selectAll(begin, end);
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>(rows0.size());
		
		for(Map<String, Object> row0 : rows0){
			Map<String, String> row = new HashMap<String, String>();
			for(Map.Entry<String, Object> entry : row0.entrySet()){
				row.put(entry.getKey(), entry.getValue().toString());
			}
			rows.add(row);
		}
		
		return rows;
	}

}
