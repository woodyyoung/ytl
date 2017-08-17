package com.lty.rt.basicData.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.basicData.bean.PjmkArea;
import com.lty.rt.basicData.treeUtil.TreeNode;

public interface PjmkAreaMapper {
    int deleteByPrimaryKey(String id);

    int insert(PjmkArea record);

    int insertSelective(PjmkArea record);

    PjmkArea selectByPrimaryKey(String id);
    
    PjmkArea selectByAreaCode(String areaCode);

    int updateByPrimaryKeySelective(PjmkArea record);

    int updateByPrimaryKey(PjmkArea record);
    
    List<PjmkArea> findListByMap(Map<String,Object> map);
    
    int batchDeleteByPrimaryKey(String id[]);
    
    int delByIds(List<String> ids);
    
    String getMaxCodeId(String parentId);
    
    List<TreeNode> getDistrictTree(Map<String,Object> map);
    
    List<PjmkArea> getChildsByParentIds(List<String> parentIdList);
    
    List<PjmkArea> getListByIds(String ids[]);
}