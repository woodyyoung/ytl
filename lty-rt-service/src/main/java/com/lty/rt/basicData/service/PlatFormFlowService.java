package com.lty.rt.basicData.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.basicData.bean.PlatForm;
import com.lty.rt.basicData.mapper.PlatFormMapper;


@Service("platFormFlowService")
public class PlatFormFlowService {
	@Autowired
	private PlatFormMapper platFormMapper;
	
	public int deleteByPrimaryKey(String id){
		return platFormMapper.deleteByPrimaryKey(id);
	}

	public int insert(PlatForm record){
		return platFormMapper.insert(record);
	}

	public PlatForm selectByPrimaryKey(String id){
		return platFormMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKey(PlatForm record){
		return platFormMapper.updateByPrimaryKey(record);
	}
	
	public List<PlatForm> findListByMap(Map<String,Object> map){
		return platFormMapper.findListByMap(map);
	}
	
}