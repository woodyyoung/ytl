package com.lty.rt.assessment.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.assessment.bean.Level;
import com.lty.rt.basicData.treeUtil.TreeNode;

public interface LevelMapper {
    int insert(Level record);

    int insertSelective(Level record);
    
    List<Level> findListByMap(Map<String, Object> map);
    
    Level findById(String id);
    
    Integer delByIds(String[] ids);
    
    int updateById(Level record);
    
    Integer findIndexLevelByLevel(String level);
    
}