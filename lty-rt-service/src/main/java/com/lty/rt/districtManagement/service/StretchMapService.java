package com.lty.rt.districtManagement.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.districtManagement.bean.StretchMap;
import com.lty.rt.districtManagement.mapper.StretchMapMapper;


@Service("stretchMapService")
public class StretchMapService{
	
	@Autowired
	private StretchMapMapper stretchMapMapper;
	
	public int insert(StretchMap record){
		return stretchMapMapper.insert(record);
	}

	public List<StretchMap> selectStretchMapByStretchId(String stretchId){
		if(StringUtils.isBlank(stretchId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":stretchId");
		}
		List<StretchMap> list = stretchMapMapper.selectStretchMapByStretchId(stretchId);
/*		if(list != null && list.size() > 0){
			list.add(list.get(0));
		}
*/		return list;
	}
	
	@Transactional
	public int batchInsert(List<StretchMap> list) {
		int count = 0;
		if(list != null && list.size() > 0){
			stretchMapMapper.delByStretchid(list.get(0).getStretchid());
			count = stretchMapMapper.batchInsert(list);
		}
		return count;
	}
	
	public int delByStretchid(String stretchId) {
		if(StringUtils.isBlank(stretchId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":stretchId");
		}
		
		int count = stretchMapMapper.delByStretchid(stretchId);
		return count;
	}
	
}