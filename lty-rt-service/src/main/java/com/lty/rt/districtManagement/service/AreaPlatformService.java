package com.lty.rt.districtManagement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.basicData.bean.PlatForm;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.districtManagement.bean.AreaPlatform;
import com.lty.rt.districtManagement.mapper.AreaPlatformMapper;
import com.lty.rt.districtManagement.vo.AreaPlatResultVo;


@Service("areaPlatformService")
public class AreaPlatformService{
	
	@Autowired
	private AreaPlatformMapper areaPlatformMapper;
	
	public int insert(AreaPlatform record){
		return areaPlatformMapper.insert(record);
	}

	public List<AreaPlatResultVo> selectAreaPlat(String areaId) throws ApplicationException{
		if(StringUtils.isBlank(areaId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":areaId");
		}
		
		List<AreaPlatResultVo> list = areaPlatformMapper.selectAreaPlat(areaId);
		return list;
	}
	
	public List<AreaPlatResultVo> findAllAvaliblePlat(){
		return areaPlatformMapper.findAllAvaliblePlat();
	}
	
	public int insertIntoPlat(String areaId, String areaCodeId, String[] platId){
		if(StringUtils.isBlank(areaId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":areaId");
		}
		if(StringUtils.isBlank(areaCodeId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":areaCodeId");
		}
		if(platId == null || platId.length <= 0){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":platId[]");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("areaId", areaId);
		map.put("areaCodeId", areaCodeId);
		map.put("platId", platId);
		int count =  areaPlatformMapper.insertIntoPlat(map);
		return count;
	}
	
	public int delByIds(String areaId, String platFormId[]) {
		if(StringUtils.isBlank(areaId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":areaId");
		}
		if(platFormId == null || platFormId.length <= 0){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":platFormId[]");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("areaId", areaId);
		map.put("platFormId", platFormId);
		int count = areaPlatformMapper.delByIds(map);
		return count;
	}

	public List<PlatForm> queryAreaPlatform(String areaId) {
		if(StringUtils.isBlank(areaId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":areaId");
		}
		
		List<PlatForm> list = areaPlatformMapper.queryAreaPlatform(areaId);
		return list;
	}

	public List<PlatForm> queryAvailablePlatform() {
		return areaPlatformMapper.queryAvailablePlatform();
	}
}