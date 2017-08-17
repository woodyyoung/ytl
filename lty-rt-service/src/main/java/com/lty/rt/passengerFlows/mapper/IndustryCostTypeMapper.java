package com.lty.rt.passengerFlows.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.passengerFlows.bean.IndustryCostType;

public interface IndustryCostTypeMapper {
	int deleteByPrimaryKey(String[] ids);

	int insert(IndustryCostType record);

	int insertSelective(IndustryCostType record);

	IndustryCostType selectByPrimaryKey(String typeNo);

	int updateByPrimaryKeySelective(IndustryCostType record);

	int updateByPrimaryKey(IndustryCostType record);

	List<Map<String, Object>> findListByMap(Map<String, Object> map);
}