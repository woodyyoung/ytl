package com.lty.rt.basicData.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.basicData.bean.PjmkArea;
import com.lty.rt.basicData.mapper.PjmkAreaMapper;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.util.UUIDUtils;
import com.lty.rt.districtManagement.bean.AreaMap;
import com.lty.rt.districtManagement.bean.AreaPlatform;
import com.lty.rt.districtManagement.mapper.AreaMapMapper;
import com.lty.rt.districtManagement.mapper.AreaPlatformMapper;
import com.lty.rt.districtManagement.service.AreaMapService;


@Service("pjmkAreaService")
public class PjmkAreaService {
	
	@Autowired
	private PjmkAreaMapper pjmkAreaMapper;
	
	@Autowired
	private AreaMapMapper areaMapMapper;
	
	@Autowired
	private AreaPlatformMapper areaPlatformMapper;
	
	@Autowired
	private AreaMapService areaMapService;
	
	public int deleteByPrimaryKey(String id){
		return pjmkAreaMapper.deleteByPrimaryKey(id);
	}

	public int insert(PjmkArea record){
		return pjmkAreaMapper.insert(record);
	}

	public PjmkArea selectByPrimaryKey(String id){
		return pjmkAreaMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKey(PjmkArea record){
		return pjmkAreaMapper.updateByPrimaryKey(record);
	}
	
	public List<PjmkArea> findListByMap(Map<String,Object> map){
		return pjmkAreaMapper.findListByMap(map);
	}
	
	/**
	 * 批量删除区域，并删除区域的经纬度关系和区域与站台的关系
	 * @param ids
	 * @return
	 */
	@Transactional
	public int batchDeleteByPrimaryKey(String ids[]){
		if(ids == null || ids.length <= 0){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":ids");
		}
		
		List<PjmkArea> list = pjmkAreaMapper.getListByIds(ids);
		List<String> delAreaIdLists = new ArrayList<String>();
		Collections.addAll(delAreaIdLists, ids);
		
		int count = 0;
		
		if(list != null && list.size() > 0){
			for(PjmkArea pjmkArea : list){
				List<String> delAreaIdList = getChildsByCodeId(pjmkArea.getCodeid());
				delAreaIdLists.addAll(delAreaIdList);
			}
		}
		
		if(delAreaIdLists != null && delAreaIdLists.size() > 0){
			//删除区域与地图区域关联关系
			areaMapMapper.delByAreaIds(delAreaIdLists);
			
			//删除区域与站台关联关系
			areaPlatformMapper.delByAreaIds(delAreaIdLists);
			
			//删区域表
			count = pjmkAreaMapper.delByIds(delAreaIdLists);
		}
		return count;
	}
	
	/**
	 * 删除区域
	 * @param ids
	 * @return
	 */
	@Transactional
	public int deleteByAreaCode(String id){
		if(StringUtils.isBlank(id)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":id"+id);
		}
		PjmkArea area = pjmkAreaMapper.selectByPrimaryKey(id);
		//查询是否有子区域
		List<String> parentIdList = new ArrayList<>();
		parentIdList.add(area.getCodeid());
    	List<PjmkArea> list = pjmkAreaMapper.getChildsByParentIds(parentIdList);
    	
    	//有子区域提示先删除子区域
    	if(list!=null&&list.size()>0){
    		throw new ApplicationException(ReturnCode.ERROR_01.getCode() , "此区域下面还关联了其他子区域，请先删除子区域");
    	}
    	
    	int count = 0;
		
    	List<String> delAreaIdLists  = new ArrayList<>();
    	delAreaIdLists.add(area.getId());
    	
		//删除区域与地图区域关联关系
		areaMapMapper.delByAreaIds(delAreaIdLists);
		
		//删除区域与站台关联关系
		areaPlatformMapper.delByAreaIds(delAreaIdLists);
		
		//删区域表
		count = pjmkAreaMapper.delByIds(delAreaIdLists);
		return count;
	}
	
	public int insertPjmkArea(PjmkArea area){
		if(StringUtils.isBlank(area.getId())){//新增
			String codeId = area.getCodeid();
			String areaCodeIdStr = "";
			if(StringUtils.isBlank(codeId)){//add一级菜单
				areaCodeIdStr = pjmkAreaMapper.getMaxCodeId("-1");
				if(StringUtils.isBlank(areaCodeIdStr)){
					areaCodeIdStr = "000";
				}
				area.setParentcodeid("-1");
			}else{//add非一级菜单
				areaCodeIdStr = pjmkAreaMapper.getMaxCodeId(codeId);
				if(StringUtils.isBlank(areaCodeIdStr)){
					areaCodeIdStr = codeId + "000";
				}
				area.setParentcodeid(codeId);
			}
			area.setId(UUIDUtils.getUUID().toString());
			BigInteger areaCodeId = new BigInteger(areaCodeIdStr);
			codeId = String.format("%0"+areaCodeIdStr.length()+"d", areaCodeId.add(new BigInteger("1")));
			area.setCodeid(codeId); //生成自已的code
			area.setLevels(codeId.length()/3);
			area.setState(0);
			pjmkAreaMapper.insert(area);
		}else{
			pjmkAreaMapper.updateByPrimaryKey(area);
		}
		
		return 1;
	}
	
	public List<TreeNode> getDistrictTree(Map<String,Object> map){
		return pjmkAreaMapper.getDistrictTree(map);
	}
	
	/**
	 * 通过codeId查到所有的子结点所在的id
	 * @param codeId
	 * @return
	 */
	public List<String> getChildsByCodeId(String codeId){
		if(StringUtils.isBlank(codeId)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":codeId");
		}
		
		//存放每次要查询的父菜单
		List<String> parentIdList = new ArrayList<String>();
		parentIdList.add(codeId);
		
		//所有要删除的结点的id
		List<String> delAreaIdList = new ArrayList<String>();
		
		//循环查询，所有以codeId为父节点的节点
        while(true){
        	//查询获取所有的子
        	List<PjmkArea> list = pjmkAreaMapper.getChildsByParentIds(parentIdList);
        	
        	parentIdList.clear();
        	if(list != null && list.size() > 0){
        		for(PjmkArea area : list){
        			parentIdList.add(area.getCodeid());
        			delAreaIdList.add(area.getId());
        		}
        	}
        	
        	if( !(parentIdList != null && parentIdList.size() > 0)){
        		break;
        	}
        }
        return delAreaIdList;
	}

	/**
	 * 保存小区信息，包括基本信息，附近站台，地图信息
	 * @param dataMap
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Transactional
	public int saveAreaInfo(Map<String, Object> dataMap) throws IllegalAccessException, InvocationTargetException {
		if(dataMap == null){
			return 0;
		}
		
		String areaId = "";
		String areaCodeId = "";
		
		//小区数据
		if(dataMap.containsKey("areaInfo")){
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> areaInfoMap = (LinkedHashMap<String, String>) dataMap.get("areaInfo");
			PjmkArea area = new PjmkArea();
			BeanUtils.populate(area, areaInfoMap);
			insertPjmkArea(area);
			areaId = area.getId();
			areaCodeId = area.getCodeid();
		}else{
			return 0;
		}
		
		//站台数据
		if(dataMap.containsKey("selectPlatInfo")){
			@SuppressWarnings("unchecked")
			ArrayList<LinkedHashMap<String, String>> selectPlatInfoList = (ArrayList<LinkedHashMap<String, String>>) dataMap.get("selectPlatInfo");
			
			//先删除
			List<String> delAreaIdLists = new ArrayList<String>();
			delAreaIdLists.add(areaId);
			areaPlatformMapper.delByAreaIds(delAreaIdLists);
			
			//在新增
			if(CollectionUtils.isNotEmpty(selectPlatInfoList)){
				List<AreaPlatform> areaPlatList = new ArrayList<AreaPlatform>();
				for(LinkedHashMap<String, String> selectPlatInfo : selectPlatInfoList){
					AreaPlatform areaPlat = new AreaPlatform();
					BeanUtils.populate(areaPlat, selectPlatInfo);
					areaPlat.setAreaId(areaId);
					areaPlat.setAreaCodeId(areaCodeId);
					areaPlatList.add(areaPlat);
				}
				areaPlatformMapper.batchInsertPlat(areaPlatList);
			}
		}
		
		//地图数据
		if(dataMap.containsKey("areaMaps")){
			@SuppressWarnings("unchecked")
			ArrayList<LinkedHashMap<String, String>> areaMapList = (ArrayList<LinkedHashMap<String, String>>) dataMap.get("areaMaps");
			if(CollectionUtils.isNotEmpty(areaMapList)){
				List<AreaMap> areaMaps = new ArrayList<AreaMap>();
				for(int i=0; i<areaMapList.size(); i++){
					AreaMap areaMap = new AreaMap();
					BeanUtils.populate(areaMap, areaMapList.get(i));
					areaMap.setAreaid(areaId);
					areaMap.setOrderby(i);
					areaMaps.add(areaMap);
				}
				areaMapService.batchInsert(areaMaps);
			}
		}
		return 1;
	}
	
}