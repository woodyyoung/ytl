package com.lty.rt.passengerFlows.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.passengerFlows.bean.IndustryCostType;
import com.lty.rt.passengerFlows.mapper.IndustryCostTypeMapper;

@Service
public class IndustryCostTypeService {
	@Autowired
	private IndustryCostTypeMapper industryCostTypeMapper;

	public IndustryCostType selectByPrimaryKey(String industryCostTypeId) {
		return industryCostTypeMapper.selectByPrimaryKey(industryCostTypeId);
	}

	public void insert(IndustryCostType industryCostType) {
		industryCostTypeMapper.insert(industryCostType);

	}

	public void deleteByPrimaryKey(String[] ids) {
		industryCostTypeMapper.deleteByPrimaryKey(ids);
	}

	public List<Map<String, Object>> findListByMap(Map<String, Object> map) {
		return industryCostTypeMapper.findListByMap(map);
	}

	public void updateByPrimaryKey(IndustryCostType industryCostType) {
		industryCostTypeMapper.updateByPrimaryKey(industryCostType);
	}

}
