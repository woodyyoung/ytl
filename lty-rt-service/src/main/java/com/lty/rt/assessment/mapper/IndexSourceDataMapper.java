package com.lty.rt.assessment.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.assessment.bean.IndexSourceData;
import com.lty.rt.assessment.vo.IndexSourceDataVo;

public interface IndexSourceDataMapper {

	int deleteIndexSourceDataByMap(Map<String, String> map);

	int batchDelByMap(Map<String, Object> map);
	
	int updateScore(IndexSourceData record);
	
	int updateIndexData(Map<String, Object> map);


	List<IndexSourceDataVo> getChartDataByDays(Map<String, Object> map);

	List<IndexSourceDataVo> getChartDataByWeeks(Map<String, Object> map);

	List<IndexSourceDataVo> getChartDataByMonths(Map<String, Object> map);

	List<IndexSourceDataVo> getChartDataByYears(Map<String, Object> map);

	Integer batchInsertIndexSourceData(List<IndexSourceData> list);

	Integer getIndexCountByMap(Map<String, String> map);
	
	List<Map<String,Object>> searchIndexData(Map<String, Object> map);
}