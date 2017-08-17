package com.lty.rt.passengerFlows.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.passengerFlows.bean.PassengerFlowLevel;
import com.lty.rt.passengerFlows.bean.PsgLevelPlan;

public interface PassengerFlowLevelMapper {
	int deleteByPrimaryKey(String id);

	int deleteByPrimaryKey(String[] ids);

	int insertPsgLevelPlan(PsgLevelPlan psgLevelPlan);

	int insertSelective(PassengerFlowLevel record);

	PassengerFlowLevel selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(PassengerFlowLevel record);

	int updateByPrimaryKey(PassengerFlowLevel record);

	List<Map<String, Object>> findListByMap(Map<String, Object> map);

	List<TreeNode> findTreeListByMap(Map<String, Object> map);

	String getMaxdataTypeId(String menuId);

	int insert(PassengerFlowLevel record);

	String getPlanByName(Map<String, Object> map);

	int batchInsert(List<PassengerFlowLevel> list);

	int deletePlan(String dataTypeId);

	int deleteByDataTypeId(String dataType);

	void closePlan(Map<String, Object> map);

	List<PsgLevelPlan> getpsgPlanAndLevelByMap(Map<String, Object> map);
}