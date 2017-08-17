package com.lty.rt.passengerFlows.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.comm.util.AddZeroUtils;
import com.lty.rt.passengerFlows.bean.IndustryCost;
import com.lty.rt.passengerFlows.mapper.IndustryCostMapper;

@Service
public class IndustryCostService {
	@Autowired
	private IndustryCostMapper industryCostMapper;

	public IndustryCost selectByPrimaryKey(String passengerFlowLevelId) {
		return industryCostMapper.selectByPrimaryKey(passengerFlowLevelId);
	}

	public void insert(IndustryCost industryCost) {
		industryCostMapper.insert(industryCost);

	}

	public void deleteByPrimaryKey(String[] ids) {
		industryCostMapper.deleteByPrimaryKey(ids);
	}

	public List<Map<String, Object>> findListByMap(Map<String, Object> map) {
		return industryCostMapper.findListByMap(map);
	}

	public void updateByPrimaryKey(IndustryCost industryCost) {
		industryCostMapper.updateByPrimaryKey(industryCost);
	}

	public Map<String, Object> getIndustryData(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> tableData = industryCostMapper.findTableDataByMap(map);
		List<Map<String, Object>> chatsData = industryCostMapper.findChatsDataByMap(map);
		List<Map<String, Object>> pieData = industryCostMapper.findPieDataByMap(map);
		Map<String, Object> zeroMap = new HashMap<String, Object>();
		zeroMap.put("COST", 0);
		List<Map<String, Object>> newChatsData = AddZeroUtils.fillHours(chatsData, zeroMap, "OCCURTIME", "TYPE_NAME");
		retMap.put("tableData", tableData);
		retMap.put("chatsData", newChatsData);
		retMap.put("pieData", pieData);
		return retMap;
	}

	public Map<String, Object> getIndustryPieData(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> pieData = industryCostMapper.findPieDataByMap(map);
		retMap.put("pieData", pieData);
		return retMap;
	}

	public IndustryCost selectByYearAndCostType(Map<String, Object> conditionMap) {
		return industryCostMapper.selectByYearAndCostType(conditionMap);
	}

}
