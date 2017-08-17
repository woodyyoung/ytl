package com.lty.rt.auxiliaryAnalysis.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.auxiliaryAnalysis.entity.FinancialType;
import com.lty.rt.auxiliaryAnalysis.mapper.FinancialTypeMapper;
import com.lty.rt.auxiliaryAnalysis.utils.UUIDGenerator;

@Service
public class FinancialTypeService {
	
	private static final Logger logger  =  LoggerFactory.getLogger(FinancialTypeService.class);

	@Autowired
	private FinancialTypeMapper financialTypeMapper;
	
	/**
	 * 查询所有
	 * */
	public Map<String, Object> getFinancialTypes(FinancialType financialType) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String,Object>> pVCostTypes = financialTypeMapper.queryFinancialTypes(financialType);
		result.put("financialTypeData", pVCostTypes);
		logger.info(FinancialTypeService.class +"- 总条数："+ pVCostTypes.size());
		return result;
	}
	
	/**
	 * 修改
	 * */
	public Map<String, Object> updFinancialType(FinancialType financialType) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			financialType.setCreate_time(new Date());
			int insertFlag = financialTypeMapper.updateFinancialType(financialType);
			result.put("insertFlag", insertFlag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("insertFlag", e.getCause().getMessage());
		}
		logger.info(FinancialTypeService.class +"- 查询信息："+ result.get("insertFlag"));
		return result;
	}
	
	/**
	 * 增加
	 * */
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> setFinancialType(FinancialType financialType) {
		Map<String, Object> flag=new HashMap<String, Object>();
		try {
			financialType.setFinancialtype_id(UUIDGenerator.getUUID());
			financialType.setCreate_time(new Date());
			financialTypeMapper.delFinancialType(financialType.getFinancialtype_id());	//增加前先删除
			int insertFlag=financialTypeMapper.insertFinancialType(financialType);
			flag.put("insertFlag", insertFlag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag.put("insertFlag", e.getCause().getMessage());
		}
		logger.info(FinancialTypeService.class +"- 修改信息："+ flag.get("insertFlag"));
		return flag;
	}
	
	/**
	 *批量 删除
	 * */
	public Map<String, Object> delFinancialTypes(List<String> ids) {
		Map<String, Object> flag=new HashMap<String, Object>();
		int delFlag=financialTypeMapper.bathDelFinancialType(ids);
		flag.put("delFlag", delFlag);
		logger.info(FinancialTypeService.class +"- 批量删除数据："+ delFlag);
		return flag;
	}

	
	/**
	 * 单个 删除
	 * */
	public Map<String, Object> delFinancialType(String financialtype_id) {
		Map<String, Object> flag=new HashMap<String, Object>();
		int delFlag=financialTypeMapper.delFinancialType(financialtype_id);
		flag.put("delFlag", delFlag);
		logger.info(FinancialTypeService.class +"- 删除数据："+ delFlag);
		return flag;
		
	}
}
