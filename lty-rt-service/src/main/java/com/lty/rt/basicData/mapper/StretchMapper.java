package com.lty.rt.basicData.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.basicData.bean.Stretch;
import com.lty.rt.basicData.treeUtil.TreeNode;

public interface StretchMapper {
	
	int deleteByPrimaryKey(String id);

    int insert(Stretch record);

    int insertSelective(Stretch record);

    Stretch selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Stretch record);

    int updateByPrimaryKey(Stretch record);
    
    List<Stretch> findListByMap(Map<String,Object> map);
    
    List<TreeNode> getStretchTree(Map<String,Object> map);
    
    int batchDeleteByPrimaryKey(String id[]);
    
    int deleteRefrencePlatform(String id[]);
    
    int deleteLngLat(String id[]);
    
    String getMaxLineId(String parentId);
}