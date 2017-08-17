package com.lty.rt.districtManagement.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.basicData.bean.PlatForm;
import com.lty.rt.districtManagement.bean.AreaPlatform;
import com.lty.rt.districtManagement.vo.AreaPlatResultVo;

public interface AreaPlatformMapper {
	int deleteByPrimaryKey(String platformid);

    int insert(AreaPlatform record);

    int insertSelective(AreaPlatform record);

    AreaPlatform selectByPrimaryKey(String platformid);

    int updateByPrimaryKeySelective(AreaPlatform record);

    int updateByPrimaryKey(AreaPlatform record);
    
    List<AreaPlatResultVo> selectAreaPlat(String areaId);
    
    List<AreaPlatResultVo> findAllAvaliblePlat();
    
    int insertIntoPlat(Map<String, Object> map);
    
    int batchInsertPlat(List<AreaPlatform> list);
    
    int delByIds(Map<String, Object> map);
    
    int delByAreaIds(List<String> list);

	List<PlatForm> queryAreaPlatform(String areaId);

	List<PlatForm> queryAvailablePlatform();
    
}