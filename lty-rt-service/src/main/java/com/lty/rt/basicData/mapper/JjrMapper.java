package com.lty.rt.basicData.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface JjrMapper {
	
	List<Map<String, Object>> selectByMonth(String month);
	
	int updateDayState(@Param("state") int state, @Param("dates") List<Date> dates, @Param("username") String username);

	List<Map<String, Object>> selectAll(@Param("begin") Date begin, @Param("end") Date end);
	
	void flushStatus();
}