package com.lty.rt.auxiliaryAnalysis.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.auxiliaryAnalysis.entity.BusRevenue;
import com.lty.rt.auxiliaryAnalysis.mapper.BusRevenueMapper;
import com.lty.rt.basicData.bean.PjmkArea;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.util.AddZeroUtils;
import com.lty.rt.comm.util.UUIDUtils;
import com.lty.rt.sysMgmt.entity.Role;
@Service
public class BusRevenueService {
	@Autowired
	private BusRevenueMapper busRevenueMapper;
	
	
	/**
	 * 查询所有
	 * @param ids
	 * @return
	 */
	public List<BusRevenue> listAll(Map<String,Object> map){
		return busRevenueMapper.listAll(map);
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public int deleteByAreaCode(String id){
		if(StringUtils.isBlank(id)){
			throw new ApplicationException(ReturnCode.ERROR_03.getCode() ,ReturnCode.ERROR_03.getMsg() + ":id"+id);
		}
		/*BusRevenue area = busRevenueMapper.selectByPrimaryKey(id);
    	busRevenueMapper.deleteByPrimaryKey(area.getId());*/
    	
    	/*String[] id = id.split(",");*/
		return busRevenueMapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 修改or增加
	 * @param ids
	 * @return
	 */
	public int insertPjmkArea(BusRevenue area){
		if(StringUtils.isBlank(area.getId())){//新增
			area.setId(UUID.randomUUID().toString());
			busRevenueMapper.deleteByYear(area.getYearStr());
			busRevenueMapper.insert(area);
		}else{
			busRevenueMapper.updateByPrimaryKey(area);
		}
		return 1;
	}

	public Map<String, Object> getIndustryData(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> chatsData = busRevenueMapper.findChatsDataByMap(map);
		List<Map<String, Object>> pieData = busRevenueMapper.findPieDataByMap(map);
		retMap.put("chatsData", chatsData);
		retMap.put("pieData", pieData);
		return retMap;
	}
	
	public Map<String, Object> getIndustryPieData(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> pieData = busRevenueMapper.findPieDataByMap(map);
		retMap.put("pieData", pieData);
		return retMap;
	}
	/*public List<BusRevenue> getquerData(Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		List<Map<String, Object>> pieData = busRevenueMapper.getquerData(retMap);
		retMap.put("pieData", pieData);
		return retMap;
	}*/
}
