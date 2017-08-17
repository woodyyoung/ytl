package com.lty.rt.districtManagement.mapper;

import java.util.List;

import com.lty.rt.districtManagement.bean.StretchMap;

public interface StretchMapMapper {
    int insert(StretchMap record);

    int insertSelective(StretchMap record);
    
    List<StretchMap> selectStretchMapByStretchId(String stretchId);
    
    int batchInsert(List<StretchMap> list);
    
    int delByStretchid(String stretchId);
}