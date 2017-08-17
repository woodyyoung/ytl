package com.lty.rt.districtManagement.mapper;

import java.util.List;

import com.lty.rt.districtManagement.bean.AreaMap;

public interface AreaMapMapper {
    int insert(AreaMap record);

    int insertSelective(AreaMap record);
    
    List<AreaMap> selectAreaMapByAreaId(String areaId);
    
    int batchInsert(List<AreaMap> list);
    
    int delByAreaid(String areaId);
    
    int delByAreaIds(List<String> list);
}