package com.lty.rt.basicData.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.basicData.bean.Line;
import com.lty.rt.basicData.mapper.LineMapper;

@Service("lineService")
public class LineService {
	
	@Autowired
	private LineMapper lineMapper;

	public List<Map<String, Object>> findListByMap(Map<String, Object> map) {
		return lineMapper.findListByMap(map);
	}
	
	public List<Line> listAllLine(){
		return lineMapper.listAllLine();
	}
	
	public Line selectByPrimaryKey(String id){
		return lineMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 包含所有数据
	 * @return
	 */
	public List<Line> listAllLineDetail(){
		return lineMapper.listAllLineDetail();
	}
	
	
	/**
	 * 属于某个部门的线路信息
	 * @return
	 */
	public List<Map<String,Object>> listAllLineDetail(String department_id){
		return lineMapper.queryLines(department_id);
	}
}