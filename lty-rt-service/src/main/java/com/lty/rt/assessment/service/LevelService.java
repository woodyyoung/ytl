package com.lty.rt.assessment.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.assessment.bean.Level;
import com.lty.rt.assessment.mapper.LevelMapper;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.util.UUIDUtils;

@Service("levelService")
public class LevelService {
	
	@Autowired
	private LevelMapper levelMapper;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Level> findLevels(){
		return levelMapper.findListByMap(new HashMap());
	}

	public Integer saveOrUpdate(Level level){
		if(level == null){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":level");
		}
		Integer count = 0;
		if(StringUtils.isBlank(level.getId())){//add
			level.setId(UUIDUtils.getUUID().toString());
			List<Level> list = levelMapper.findListByMap(null);
			for(Level l:list){
				if(l.getLevels().equals(level.getLevels())){
					throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,"评价等级已存在!");
				}
			}
			count = levelMapper.insert(level);
		}else{//edit
			count = levelMapper.updateById(level);
		}
		return count;
	}
	
	public Level findById(String id){
		if(StringUtils.isBlank(id)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":id");
		}
		return levelMapper.findById(id);
	}
	
	public Integer delByIds(String ids){
		if(StringUtils.isBlank(ids)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":ids");
		}
		String id = ids.split(",")[0];
		Level level = levelMapper.findById(id);
		Integer findIndexLevelByLevel = levelMapper.findIndexLevelByLevel(level.getLevels());
		if(findIndexLevelByLevel!=null&&findIndexLevelByLevel>0){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,"此评价等级已被使用中，不能删除！");
		}
		return levelMapper.delByIds(ids.split(","));
	}
}
