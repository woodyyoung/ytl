package com.lty.rt.auxiliaryAnalysis.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.auxiliaryAnalysis.entity.FinancialType;

public interface FinancialTypeMapper {
	
	List<Map<String,Object>> queryFinancialTypes(FinancialType financialType);

	int insertFinancialType(FinancialType financialType);

	int updateFinancialType(FinancialType financialType);

	int delFinancialType(String financialtype_id);

	int bathDelFinancialType(List<String> list);

	int delPVCostTypeByCondition(FinancialType financialType);

}
