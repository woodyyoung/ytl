package com.lty.rt.basicData.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.basicData.bean.Station;
import com.lty.rt.basicData.mapper.StationMapper;

@Service("stationService")
public class StationService {
	@Autowired
	private StationMapper stationMapper;
	public int deleteByPrimaryKey(String id) {
		return stationMapper.deleteByPrimaryKey(id);
	}

	public int insert(Station record) {
		return stationMapper.insert(record);
	}

	public int insertSelective(Station record) {
		return stationMapper.insertSelective(record);
	}

	public Station selectByPrimaryKey(String id) {
		return stationMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(Station record) {
		return stationMapper.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(Station record) {
		return stationMapper.updateByPrimaryKey(record);
	}

	public List<Station> queryStationByLineId(String lineId) {
		return stationMapper.queryStationByLineId(lineId);
	}
	
	public List<Station> queryStationBySchemeId(String schemeId) {
		return stationMapper.queryStationBySchemeId(schemeId);
	}
}
