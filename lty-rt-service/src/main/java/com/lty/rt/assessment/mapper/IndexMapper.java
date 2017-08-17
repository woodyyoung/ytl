package com.lty.rt.assessment.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.assessment.bean.Index;
import com.lty.rt.assessment.vo.IndexTotalDataVo;
import com.lty.rt.basicData.treeUtil.TreeNode;

public interface IndexMapper {
	int deleteByPrimaryKey(String pkid);

    int insert(Index record);

    int insertSelective(Index record);

    Index selectByPrimaryKey(String pkid);

    int updateByPrimaryKeySelective(Index record);

    int updateByPrimaryKey(Index record);
    
    List<Index> getLists(Integer indexType);
    
    List<Index> getListByMap(Map<String, Object> map);
    
    List<TreeNode> getIndexTree(Map<String, Object> map);
    
    String getMaxId(String parentId);
    
    Integer delByIds(String[] ids);
    
    Integer countIndexSourceData(String indexId);
    
    Index getIndexByID(String id);
    
    List<IndexTotalDataVo> getTotalChartDataByDays(Map<String, Object> map);
    
    List<IndexTotalDataVo> getDetailChartDataByDays(Map<String, Object> map);
    
    List<IndexTotalDataVo> getTotalChartDataByWeeks(Map<String, Object> map);
    
    List<IndexTotalDataVo> getDetailChartDataByWeeks(Map<String, Object> map);
    
    List<IndexTotalDataVo> getTotalChartDataByMonths(Map<String, Object> map);
    
    List<IndexTotalDataVo> getDetailChartDataByMonths(Map<String, Object> map);
    
    List<IndexTotalDataVo> getTotalChartDataByYears(Map<String, Object> map);
    
    List<IndexTotalDataVo> getDetailChartDataByYears(Map<String, Object> map);
}