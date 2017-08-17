package com.lty.rt.auxiliaryAnalysis.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.auxiliaryAnalysis.entity.PVCost;

public interface PVCostMapper {
	
	List<Map<String,Object>> queryPVCosts(PVCost param);

	int insertPVCost(PVCost pVCost);

	int updatePVCost(PVCost pVCost);

	int delPVCostByPvcost_id(String pVCost_id);

	int bathDelPVCost(List<String> list);
	
	List<Map<String,Object>> queryDepartments();
	
	List<Map<String,Object>> queryLines(String department_id);
	
	int delPVCostByCondition(PVCost pVCost);
	
	List<Map<String,Object>> queryPVCostsByCondition(PVCost param);


}
