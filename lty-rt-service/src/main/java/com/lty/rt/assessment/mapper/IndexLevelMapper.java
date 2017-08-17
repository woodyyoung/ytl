package com.lty.rt.assessment.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.assessment.bean.IndexLevel;

public interface IndexLevelMapper {
    int insert(IndexLevel record);

    int insertSelective(IndexLevel record);
    
    Integer batchInsert(List<IndexLevel> list);
    
    Integer update(IndexLevel indexLevel);
    
    Integer delByIndexId(String indexId[]);
    
    List<IndexLevel> getListByMap(Map<String, Object> map);
}