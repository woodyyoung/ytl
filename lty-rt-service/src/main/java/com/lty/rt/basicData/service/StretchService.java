package com.lty.rt.basicData.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.basicData.bean.Stretch;
import com.lty.rt.basicData.mapper.StretchMapper;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.util.UUIDUtils;


@Service("stretchService")
public class StretchService {
	
	@Autowired
	private StretchMapper stretchMapper;
	
	public List<Stretch> findListByMap(Map<String,Object> map){
		return stretchMapper.findListByMap(map);
	}
	
	public List<TreeNode> getStretchTree(Map<String,Object> map){
		return stretchMapper.getStretchTree(map);
	}
	
	@Transactional
	public int batchDeleteByPrimaryKey(String id[]){ 
		if(id == null || id.length <= 0){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":id");
		}
		int count = stretchMapper.batchDeleteByPrimaryKey(id);//删除基本信息
		stretchMapper.deleteRefrencePlatform(id);//删除关联站台
		stretchMapper.deleteLngLat(id);//删除经纬度
		return count;
	}
	
	public Stretch selectByPrimaryKey(String id){
		if(StringUtils.isBlank(id)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":id");
		}
		return stretchMapper.selectByPrimaryKey(id);
	}
	
	public int insertStretch(Stretch stretch){
		if(stretch == null){
			throw new ApplicationException(ReturnCode.ERROR_02.getCode() ,ReturnCode.ERROR_02.getMsg() + ":stretch");
		}
		
		int count = 0;
		if(StringUtils.isBlank(stretch.getId())){//新增
			String lineId = stretch.getLineid();
			String lineIdStr = "";
			if(StringUtils.isBlank(lineId)){//add一级菜单
				lineIdStr = stretchMapper.getMaxLineId("-1");
				if(StringUtils.isBlank(lineIdStr)){
					lineIdStr = "000";
				}
				stretch.setParentlineid("-1");
			}else{//add非一级菜单
				lineIdStr = stretchMapper.getMaxLineId(lineId);
				if(StringUtils.isBlank(lineIdStr)){
					lineIdStr = lineId + "000";
				}
				stretch.setParentlineid(lineId);
			}
			stretch.setId(UUIDUtils.getUUID().toString());
			Integer areaCodeId = Integer.valueOf(lineIdStr);
			stretch.setLineid(String.format("%0"+lineIdStr.length()+"d", areaCodeId + 1)); //生成自已的code
			stretch.setLevels(lineId.length()/3);
			stretch.setState(0);
			count = stretchMapper.insert(stretch);
		}else{
			count = stretchMapper.updateByPrimaryKey(stretch);
		}
		
		return count;
	}

}