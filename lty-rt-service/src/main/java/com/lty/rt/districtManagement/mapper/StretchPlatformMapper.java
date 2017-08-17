package com.lty.rt.districtManagement.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.districtManagement.bean.StretchPlatform;
import com.lty.rt.districtManagement.vo.AreaPlatResultVo;
import com.lty.rt.passengerFlows.bean.StretchPlatFormLine;

public interface StretchPlatformMapper {
	int deleteByPrimaryKey(String platformid);

	int insert(StretchPlatform record);

	int insertSelective(StretchPlatform record);

	StretchPlatform selectByPrimaryKey(String platformid);

	int updateByPrimaryKeySelective(StretchPlatform record);

	int updateByPrimaryKey(StretchPlatform record);

	List<AreaPlatResultVo> selectStretchPlat(String lineId);

	List<Map<String, Object>> selectStretchPlatform(String lineId);

	List<Map<String, Object>> findNotRefrencePlatfom(String lineId);

	List<Map<String, Object>> findAllAvaliblePlat();

	int insertIntoPlat(Map<String, Object> map);

	int delByIds(Map<String, Object> map);

	List<StretchPlatFormLine> selectStretchAndPlat(String id);
}