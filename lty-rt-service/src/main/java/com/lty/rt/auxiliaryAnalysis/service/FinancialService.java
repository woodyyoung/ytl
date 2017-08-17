package com.lty.rt.auxiliaryAnalysis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.auxiliaryAnalysis.entity.Financial;
import com.lty.rt.auxiliaryAnalysis.mapper.FinancialMapper;
import com.lty.rt.auxiliaryAnalysis.utils.UUIDGenerator;

@Service
public class FinancialService {
	
	private static final Logger logger  =  LoggerFactory.getLogger(FinancialService.class);
	
	@Autowired
	private FinancialMapper financialMapper;
	
	/**
	 * 查询所有
	 * */
	public Map<String, Object> getFinancials(Financial param) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String,Object>> stationData = financialMapper.queryFinancials(param);
		logger.info(FinancialService.class +"- 总条数："+ stationData.size());
		result.put("financialData", stationData);
		return result;
	}
	
	/**
	 * 增加
	 * */
	public Map<String, Object> setFinancial(Financial financial) {
		Map<String, Object> flag=new HashMap<String, Object>();
		try {
			String errorMes=financial.fieldIsEmpty();
			if("".equals(errorMes)){
				financial.setFinancial_id(UUIDGenerator.getUUID());
				financial.setCreate_time(new Date());

				int insertFlag=financialMapper.insertFinancial(financial);
				flag.put("insertFlag", insertFlag);
			}else{
				flag.put("insertFlag", errorMes);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag.put("insertFlag", e.getCause().getMessage());
		}
		logger.info(FinancialService.class +"- 查询信息："+ flag.get("insertFlag"));
		return flag;
	}
	
	
	
	
	/**
	 * 修改
	 * */
	public Map<String, Object> updFinancial(Financial financial) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String errorMes=financial.fieldIsEmpty();
			if("".equals(errorMes)){
				financial.setCreate_time(new Date());

				int insertFlag = financialMapper.updateFinancial(financial);
				result.put("insertFlag", insertFlag);
			}else{
				result.put("insertFlag", errorMes);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("insertFlag", e.getCause().getMessage());
		}
		logger.info(FinancialService.class +"- 修改信息："+ result.get("insertFlag"));
		return result;
	}
	
	
	/**
	 * 删除
	 * */
	public Map<String, Object> delFinancial(String string) {
		Map<String, Object> flag=new HashMap<String, Object>();
		int delFlag=financialMapper.delFinancialByFinancial_id(string);
		logger.info(FinancialService.class +"- 删除数据："+ delFlag);
		flag.put("delFlag", delFlag);
		return flag;
	}
	
	
	
	/**
	 * 根据起始时间和结束时间查询种类
	 * */ 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> queryLineFinancialsByCondition(Financial financial){
		Map<String,Object> data = new HashMap<String,Object>(); 
		
		List<Map<String,Object>> source=financialMapper.queryLineFinancialsByCondition(financial);
		Set<String> years = new TreeSet<String>();
		List<Map<String,Object>> ll = new ArrayList<Map<String,Object>>(); 
		Map<String,Object> mm = new HashMap<String,Object>();
		String before = "";
		for (Map<String, Object> map : source) {
			String financialtype_id= String.valueOf(map.get("NAME"));
			if(!financialtype_id.equals(before) && !"".equals(before)){
				ll.add(mm);
			}
			before=financialtype_id;
			
			if(!mm.isEmpty() &&  mm.get("name").equals(financialtype_id)){
				((List) mm.get("data")).add(String.valueOf(map.get("MONEY")));
			}else{
				mm = new HashMap<String,Object>();
				List<String> array = new ArrayList<String>();
				array.add(String.valueOf(map.get("MONEY")));
				mm.put("data", array);
				mm.put("name", String.valueOf(map.get("NAME")));
				mm.put("stack", String.valueOf(map.get("STACK")));
				mm.put("type", String.valueOf(map.get("TYPE")));
			}
			years.add(String.valueOf(map.get("YEAR")));
		}
		if(!mm.isEmpty()){
			ll.add(mm);
		}
		
		data.put("YEARS", years);
		data.put("DATA", ll);
		
		return data;
	}
	
	
	/**
	 * 根据起始时间和结束时间查询种类
	 * */ 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> queryPieFinancialsByCondition(Financial financial){
		Map<String,Object> data = new HashMap<String,Object>(); 
		
		List<Map<String,Object>> source=financialMapper.queryPieFinancialsByCondition(financial);
		Set<String> types = new TreeSet<String>();
		List<Map<String,Object>> ll = new ArrayList<Map<String,Object>>(); 
		Map<String,Object> mm = new HashMap<String,Object>();
		String before = "";
		for (Map<String, Object> map : source) {
			String financialtype_id= String.valueOf(map.get("NAME"));
			if(!financialtype_id.equals(before) && !"".equals(before)){
				ll.add(mm);
			}
			before=financialtype_id;
			
			if(!mm.isEmpty() &&  mm.get("name").equals(financialtype_id)){
				((List) mm.get("data")).add(String.valueOf(map.get("VALUE")));
			}else{
				mm = new HashMap<String,Object>();
				List<String> array = new ArrayList<String>();
				array.add(String.valueOf(map.get("VALUE")));
				mm.put("value", array);
				mm.put("name", String.valueOf(map.get("NAME")));
			}
			types.add(String.valueOf(map.get("NAME")));
		}
		if(!mm.isEmpty()){
			ll.add(mm);
		}
		
		data.put("TYPES", types);
		data.put("DATA", ll);
		
		return data;
	}
}
