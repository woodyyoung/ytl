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

import com.lty.rt.auxiliaryAnalysis.entity.PVCostType;
import com.lty.rt.auxiliaryAnalysis.mapper.PVCostTypeMapper;
import com.lty.rt.auxiliaryAnalysis.utils.UUIDGenerator;

@Service
public class PVCostTypeService {
	
	private static final Logger logger  =  LoggerFactory.getLogger(PVCostTypeService.class);

	
	@Autowired
	private PVCostTypeMapper pVCostTypeMapper;
	
	/**
	 * 查询所有
	 * */
	public List<Map<String,Object>> getPVCostTypes() {
		List<Map<String,Object>> pVCostTypes = pVCostTypeMapper.queryPVCostTypes();
		logger.info(PVCostTypeService.class +"- 总条数："+ pVCostTypes.size());
		return pVCostTypes;
	}
	
	/**
	 * 修改
	 * */
	public Map<String, Object> updPVCostType(PVCostType pVCostType) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			pVCostType.setCreate_time(new Date());
			int insertFlag = pVCostTypeMapper.updatePVCostType(pVCostType);
			result.put("insertFlag", insertFlag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("insertFlag", e.getCause().getMessage());
		}
		logger.info(PVCostTypeService.class +"- 查询信息："+ result.get("insertFlag"));
		return result;
	}
	
	/**
	 * 增加
	 * */
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> setPVCostType(PVCostType pVCostType) {
		Map<String, Object> flag=new HashMap<String, Object>();
		try {
			pVCostType.setPvcosttype_id(UUIDGenerator.getUUID());
			pVCostType.setCreate_time(new Date());
			pVCostTypeMapper.delPVCostTypeByCondition(pVCostType);	//增加前先删除
			int insertFlag=pVCostTypeMapper.insertPVCostType(pVCostType);
			flag.put("insertFlag", insertFlag);
		} catch (Exception e) {
			e.printStackTrace();
			flag.put("insertFlag", e.getCause().getMessage());
		}
		logger.info(PVCostTypeService.class +"- 修改信息："+ flag.get("insertFlag"));
		return flag;
	}
	
	/**
	 * 删除多个
	 * */
	public Map<String, Object> delPVCostTypes(List<String> ids) {
		Map<String, Object> flag=new HashMap<String, Object>();
		int delFlag=pVCostTypeMapper.bathDelPVCostType(ids);
		flag.put("delFlag", delFlag);
		logger.info(PVCostTypeService.class +"- 删除数据："+ delFlag);
		return flag;
	}
	
	/**
	 * 删除单个
	 * */
	public Map<String, Object> delPVCostType(String pvcosttype_id) {
		Map<String, Object> flag=new HashMap<String, Object>();
		int delFlag=pVCostTypeMapper.delPVCostType(pvcosttype_id);
		flag.put("delFlag", delFlag);
		logger.info(PVCostTypeService.class +"- 删除数据："+ delFlag);
		return flag;
	}
}
