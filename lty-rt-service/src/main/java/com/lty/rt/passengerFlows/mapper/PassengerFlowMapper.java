package com.lty.rt.passengerFlows.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.passengerFlows.bean.PassengerFlow;

public interface PassengerFlowMapper {
	int deleteByPrimaryKey(String id);

	int insert(PassengerFlow record);

	int insertSelective(PassengerFlow record);

	PassengerFlow selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(PassengerFlow record);

	int updateByPrimaryKey(PassengerFlow record);

	List<Map<String, Object>> findListByMap(Map<String, Object> map);
}