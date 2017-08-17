package com.lty.rt.basicData.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.basicData.bean.Line;

public interface LineMapper {
	int deleteByPrimaryKey(String id);

	int insert(Line record);

	int insertSelective(Line record);

	Line selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(Line record);

	int updateByPrimaryKey(Line record);

	List<Line> listAllLine();
	
	List<Map<String,Object>> findListByMap(Map<String,Object> map);

	List<Line> listAllLineDetail();
	
	List<Map<String,Object>> queryLines(String department_id);
}