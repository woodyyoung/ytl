package com.lty.rt.auxiliaryAnalysis.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.auxiliaryAnalysis.entity.BusRevenue;

public interface BusRevenueMapper {
    int deleteByPrimaryKey(String id);
    
    int deleteByYear(String date);

    int insert(BusRevenue record);

    int insertSelective(BusRevenue record);

    BusRevenue selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BusRevenue recRord);

    int updateByPrimaryKey(BusRevenue record);
    
    List<BusRevenue> listAll(Map<String, Object> map);

	List<Map<String, Object>> findChatsDataByMap(Map<String, Object> map);

	List<Map<String, Object>> findPieDataByMap(Map<String, Object> map);
	
	List<Map<String, Object>> CaiZhengDataByMap(Map<String, Object> map);
}