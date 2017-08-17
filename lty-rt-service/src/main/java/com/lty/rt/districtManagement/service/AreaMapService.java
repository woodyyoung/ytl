package com.lty.rt.districtManagement.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.districtManagement.bean.AreaMap;
import com.lty.rt.districtManagement.mapper.AreaMapMapper;


@Service("areaMapService")
public class AreaMapService{
	
	@Autowired
	private AreaMapMapper areaMapMapper;
	
	public int insert(AreaMap record){
		return areaMapMapper.insert(record);
	}

	public List<AreaMap> selectAreaMapByAreaId(String areaId){
		if(StringUtils.isBlank(areaId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":areaId");
		}
		List<AreaMap> list = areaMapMapper.selectAreaMapByAreaId(areaId);
		if(list != null && list.size() > 0){
			list.add(list.get(0));
		}
		return list;
	}
	
	@Transactional
	public int batchInsert(List<AreaMap> list) {
		int count = 0;
		if(list != null && list.size() > 0){
			areaMapMapper.delByAreaid(list.get(0).getAreaid());
			count = areaMapMapper.batchInsert(list);
		}
		return count;
	}
	
	public int delByAreaid(String areaId){
		if(StringUtils.isBlank(areaId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":areaId");
		}
		int count = areaMapMapper.delByAreaid(areaId);
		return count;
	}
	
}