package com.lty.rt.passengerFlows.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.passengerFlows.bean.IndustryCost;

public interface IndustryCostMapper {
	int deleteByPrimaryKey(String[] ids);

	int insert(IndustryCost record);

	int insertSelective(IndustryCost record);

	IndustryCost selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(IndustryCost record);

	int updateByPrimaryKey(IndustryCost record);

	List<Map<String, Object>> findListByMap(Map<String, Object> map);

	List<Map<String, Object>> findTableDataByMap(Map<String, Object> map);

	List<Map<String, Object>> findChatsDataByMap(Map<String, Object> map);

	List<Map<String, Object>> findPieDataByMap(Map<String, Object> map);

	IndustryCost selectByYearAndCostType(Map<String, Object> conditionMap);
}