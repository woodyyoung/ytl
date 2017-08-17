package com.lty.rt.auxiliaryAnalysis.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.auxiliaryAnalysis.entity.Financial;

public interface FinancialMapper {
	
	List<Map<String,Object>> queryFinancials(Financial param);

	int insertFinancial(Financial financial);

	int updateFinancial(Financial financial);

	int delFinancialByFinancial_id(String financial_id);

	int bathDelFinancial(List<String> list);
	
	int delFinancialByCondition(Financial financial);
	
	List<Map<String,Object>> queryLineFinancialsByCondition(Financial financial);
	
	List<Map<String,Object>> queryPieFinancialsByCondition(Financial financial);

}
