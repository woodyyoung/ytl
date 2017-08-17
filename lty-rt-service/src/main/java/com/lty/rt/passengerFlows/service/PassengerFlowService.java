package com.lty.rt.passengerFlows.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.passengerFlows.bean.PassengerFlow;
import com.lty.rt.passengerFlows.mapper.PassengerFlowMapper;

@Service("myPassengerFlowService")
public class PassengerFlowService {
	@Autowired
	private PassengerFlowMapper myPassengerFlow;

	public int deleteByPrimaryKey(String id) {
		return myPassengerFlow.deleteByPrimaryKey(id);
	}

	public int insert(PassengerFlow record) {
		return myPassengerFlow.insert(record);
	}

	public PassengerFlow selectByPrimaryKey(String id) {
		return myPassengerFlow.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKey(PassengerFlow record) {
		return myPassengerFlow.updateByPrimaryKey(record);
	}

	public List<Map<String, Object>> findListByMap(Map<String, Object> map) {
		return myPassengerFlow.findListByMap(map);
	}

}