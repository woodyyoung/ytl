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
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.auxiliaryAnalysis.entity.PVCost;
import com.lty.rt.auxiliaryAnalysis.mapper.PVCostMapper;
import com.lty.rt.auxiliaryAnalysis.utils.UUIDGenerator;

/**
 * 公交行业人车成本 业务类
 * */
@Service
public class PVCostService {
	
	private static final Logger logger  =  LoggerFactory.getLogger(PVCostService.class);

	@Autowired
	private PVCostMapper pVcostMapper; 
	
	/**
	 * 查询所有
	 * */
	public List<Map<String,Object>> getPVCosts(PVCost param) {
		List<Map<String,Object>> stationData = pVcostMapper.queryPVCosts(param);
		logger.info(PVCostService.class +"- 查询所有总条数："+ stationData.size());
		return stationData;
	}
	
	/**
	 * 查询所有部门数据
	 * */
	public List<Map<String,Object>> getDepartments() {
		List<Map<String,Object>> departments = pVcostMapper.queryDepartments();
		logger.info(PVCostService.class +"- 查询所有部门数据总条数："+ departments.size());
		return departments;
	}
	
	
	/**
	 * 增加
	 * */
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> setPVCost(PVCost pVCost) {
		Map<String, Object> flag=new HashMap<String, Object>();
		try {
			String errorMes=pVCost.fieldIsEmpty();
			if("".equals(errorMes)){
				pVCost.setPvcost_id(UUIDGenerator.getUUID());
				pVCost.setCreate_time(new Date());
				pVcostMapper.delPVCostByCondition(pVCost);	//增加前先删除
				int insertFlag=pVcostMapper.insertPVCost(pVCost);
				flag.put("insertFlag", insertFlag);
			}else{
				flag.put("insertFlag", errorMes);
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag.put("insertFlag", e.getCause().getMessage());
		}
		logger.info(PVCostService.class +"- 查询信息："+ flag.get("insertFlag"));
		return flag;
	}
	
	
	
	
	/**
	 * 修改
	 * */
	public Map<String, Object> updPVCost(PVCost pVCost) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String errorMes=pVCost.fieldIsEmpty();
			if("".equals(errorMes)){
				pVCost.setCreate_time(new Date());
				int insertFlag = pVcostMapper.updatePVCost(pVCost);
				result.put("insertFlag", insertFlag);
			}else{
				result.put("insertFlag", errorMes);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("insertFlag", e.getCause().getMessage());
		}
		logger.info(PVCostService.class +"- 修改信息："+ result.get("insertFlag"));
		return result;
	}
	
	
	/**
	 * 删除多个
	 * */
	public Map<String, Object> bathDelPVCost(List<String> ids) {
		Map<String, Object> flag=new HashMap<String, Object>();
		int delFlag=pVcostMapper.bathDelPVCost(ids);
		flag.put("delFlag", delFlag);
		logger.info(PVCostService.class +"- 删除数据："+ delFlag);
		return flag;
	}
	
	/**
	 * 删除单个
	 * */
	public Map<String, Object> delPVCost(String pVCost_id) {
		Map<String, Object> flag=new HashMap<String, Object>();
		int delFlag=pVcostMapper.delPVCostByPvcost_id(pVCost_id);
		flag.put("delFlag", delFlag);
		logger.info(PVCostService.class +"- 删除数据："+ delFlag);
		return flag;
	}
	
	/**
	 * 根据线路ID，公司ID，起始年份，结束年份查询
	 * */ 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> queryPVCostsByCondition(PVCost param){
		Map<String,Object> data = new HashMap<String,Object>(); 
		
		List<Map<String,Object>> source=pVcostMapper.queryPVCostsByCondition(param);
		Set<String> years = new TreeSet<String>();
		List<Map<String,Object>> ll = new ArrayList<Map<String,Object>>(); 
		Map<String,Object> mm = new HashMap<String,Object>();
		String before = "";
		for (Map<String, Object> map : source) {
			String pvcosttype_id= String.valueOf(map.get("NAME"));
			if(!pvcosttype_id.equals(before) && !"".equals(before)){
				ll.add(mm);
			}
			before=pvcosttype_id;
			
			if(!mm.isEmpty() &&  mm.get("name").equals(pvcosttype_id)){
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
	
}
