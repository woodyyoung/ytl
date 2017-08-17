package com.lty.rt.basicData.mapper;

import java.util.List;

import com.lty.rt.basicData.bean.Station;

public interface StationMapper {
	int deleteByPrimaryKey(String id);

	int insert(Station record);

	int insertSelective(Station record);

	Station selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(Station record);

	int updateByPrimaryKey(Station record);

	List<Station> queryStationByLineId(String lineId);
	
	List<Station> queryStationBySchemeId(String schemeId);
	
	List<Station> selectAllStation();
}