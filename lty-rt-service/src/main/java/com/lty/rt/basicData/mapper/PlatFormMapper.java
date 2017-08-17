package com.lty.rt.basicData.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.basicData.bean.PlatForm;


public interface PlatFormMapper {
	int deleteByPrimaryKey(String id);

	int insert(PlatForm record);

	int insertSelective(PlatForm record);

	PlatForm selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(PlatForm record);

	int updateByPrimaryKey(PlatForm record);

	List<PlatForm> findListByMap(Map<String, Object> map);

	List<PlatForm> listAllPlatform();
}