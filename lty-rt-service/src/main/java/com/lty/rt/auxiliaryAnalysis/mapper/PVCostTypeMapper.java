package com.lty.rt.auxiliaryAnalysis.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.auxiliaryAnalysis.entity.PVCostType;

public interface PVCostTypeMapper {
	
	List<Map<String,Object>> queryPVCostTypes();

	int insertPVCostType(PVCostType pVCostType);

	int updatePVCostType(PVCostType pVCostType);

	int delPVCostType(String pvcosttype_id);

	int bathDelPVCostType(List<String> list);

	int delPVCostTypeByCondition(PVCostType pVCostType);
}
