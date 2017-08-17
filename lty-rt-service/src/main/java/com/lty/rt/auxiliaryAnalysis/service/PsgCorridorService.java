package com.lty.rt.auxiliaryAnalysis.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.auxiliaryAnalysis.mapper.PsgCorridorMapper;

/**
 * 客运走廊分析
 * @author Administrator
 *
 */
@Service
public class PsgCorridorService {
	@Autowired
	private PsgCorridorMapper psgCorridorMapper;
	
	public Map<String, Object> queryPsgCorridorByDay(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		String areaCode = (String) params.get("areaCode");
		if(StringUtils.isEmpty(areaCode)){
			return null;
		}
		
		//小区信息
		List<Map<String, Object>> list = psgCorridorMapper.queryAreaInfo(areaCode);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		
		//获取需要连接的小区信息
		List<Map<String, String>> areaInfoList = getAreaInfoList(list);
		
		//获取相邻小区之间关系
		List<String> adjoinList = getAreaAdjoinList(list);
		
		//获取关联小区的code
		String areaCodeStr = getAreaCodeStr(list);
		params.put("areaCodeStr", areaCodeStr);
		
		//查询流出TOP
		List<Map<String, Object>> flowOut = psgCorridorMapper.queryFlowOutTop(params);
		
		//查询流入TOP
		List<Map<String, Object>> flowIn = psgCorridorMapper.queryFlowInTop(params);
		
		//查询客流分析数据
		List<Map<String, Object>> flowAnalysis = psgCorridorMapper.queryFlowAnalysisByDay(params);
		
		//查询相邻小区客流情况
		Map<String, Integer> adjoinAreaFlow = queryAdjoinAreaFlow(params, adjoinList);
		
		result.put("adjoinList", adjoinList);
		result.put("areaInfoList", areaInfoList);
		result.put("flowOut", flowOut);
		result.put("flowIn", flowIn);
		result.put("flowAnalysis", flowAnalysis);
		result.put("adjoinAreaFlow", adjoinAreaFlow);
		
		return result;
	}
	
	/**
	 * 获取地图绘制小区的code
	 * @param list
	 * @return
	 */
	private String getAreaCodeStr(List<Map<String, Object>> list) {
		StringBuilder sb = new StringBuilder();
		for(Map<String, Object> map : list){
			String code = (String) map.get("CODEID");
			if(sb.indexOf(code) < 0){
				sb.append("'").append(code).append("',");
			}
		}
		return sb.substring(0, sb.length()-1);
	}

	/**
	 * 查询相邻小区之间客流
	 * @param params
	 * @param adjoinList
	 * @return
	 */
	private Map<String, Integer> queryAdjoinAreaFlow(Map<String, Object> params, List<String> adjoinList) {
		if(CollectionUtils.isEmpty(adjoinList)){
			return null;
		}
		
		Map<String, Integer> adjoinAreaFlow = new HashMap<String, Integer>();
		for(String adjoin : adjoinList){
			String[] areaIdArr = adjoin.split(",");
			params.put("source", areaIdArr[0]);
			params.put("target", areaIdArr[1]);
			int flow = psgCorridorMapper.queryTotleAreaODByDay(params);
			adjoinAreaFlow.put(adjoin, flow);
			
			String adjoinR = areaIdArr[1]+","+areaIdArr[0];
			if(!adjoinList.contains(adjoinR)){
				params.put("source", areaIdArr[1]);
				params.put("target", areaIdArr[0]);
				flow = psgCorridorMapper.queryTotleAreaODByDay(params);
				adjoinAreaFlow.put(adjoinR, flow);
			}
		}
		return adjoinAreaFlow;
	}

	/**
	 * 获取相邻小区之间关系
	 * @param list
	 * @return
	 */
	private List<String> getAreaAdjoinList(List<Map<String, Object>> list) {
		List<String> adjoinList = new LinkedList<String>();
		for(int i=0; i<list.size()-1;  i++){
			String codeId0 = (String) list.get(i).get("CODEID");
			BigDecimal gprsId0 = (BigDecimal) list.get(i).get("GPRSID");
			String codeId1 = (String) list.get(i+1).get("CODEID");
			BigDecimal gprsId1 = (BigDecimal) list.get(i+1).get("GPRSID");
			
			if((!codeId0.equals(codeId1)) && (gprsId0.compareTo(gprsId1) == 0)){
				if((!adjoinList.contains(codeId0+","+codeId1))){
					adjoinList.add(codeId0+","+codeId1);
				}
			}
		}
		return adjoinList;
	}

	/**
	 * 获取需要连接的小区信息
	 * @param list
	 * @return
	 */
	private List<Map<String, String>> getAreaInfoList(List<Map<String, Object>> list) {
		List<Map<String, String>> areaInfoList = new LinkedList<Map<String, String>>();
		List<String> areaCodeList = new LinkedList<String>();
		for(Map<String, Object> infoMap : list){
			String codeId = (String) infoMap.get("CODEID");
			if(!areaCodeList.contains(codeId)){
				Map<String, String> areaInfo = new HashMap<String, String>();
				areaInfo.put("areaId", codeId);
				areaInfo.put("lan", (String) infoMap.get("LAN"));
				areaInfo.put("areaName", (String) infoMap.get("CODENAME"));
				areaCodeList.add(codeId);
				areaInfoList.add(areaInfo);
			}
		}
		return areaInfoList;
	}

	/**
	 * 按小时查询客运走廊数据
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryPsgCorridorByHour(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		String areaCode = (String) params.get("areaCode");
		if(StringUtils.isEmpty(areaCode)){
			return null;
		}
		
		//小区信息
		List<Map<String, Object>> list = psgCorridorMapper.queryAreaInfo(areaCode);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		
		//获取需要连接的小区信息
		List<Map<String, String>> areaInfoList = getAreaInfoList(list);
		
		//获取相邻小区之间关系
		List<String> adjoinList = getAreaAdjoinList(list);
		
		//获取关联小区的code
		String areaCodeStr = getAreaCodeStr(list);
		params.put("areaCodeStr", areaCodeStr);
		
		//查询流出TOP
		List<Map<String, Object>> flowOut = psgCorridorMapper.queryFlowOutTop(params);
		
		//查询流入TOP
		List<Map<String, Object>> flowIn = psgCorridorMapper.queryFlowInTop(params);
		
		//查询客流分析数据
		List<Map<String, Object>> flowAnalysis = psgCorridorMapper.queryFlowAnalysisByHour(params);
		
		//查询相邻小区客流情况
		Map<String, Integer> adjoinAreaFlow = queryAdjoinAreaFlow(params, adjoinList);
		
		result.put("adjoinList", adjoinList);
		result.put("areaInfoList", areaInfoList);
		result.put("flowOut", flowOut);
		result.put("flowIn", flowIn);
		result.put("flowAnalysis", flowAnalysis);
		result.put("adjoinAreaFlow", adjoinAreaFlow);
		
		return result;
	}

	/**
	 * 按周查询客运走廊数据
	 * @param params
	 * @return
	 * @throws ParseException 
	 */
	public Map<String, Object> queryPsgCorridorByWeek(Map<String, Object> params) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		String areaCode = (String) params.get("areaCode");
		if(StringUtils.isEmpty(areaCode)){
			return null;
		}
		
		//小区信息
		List<Map<String, Object>> list = psgCorridorMapper.queryAreaInfo(areaCode);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
		String beginDate = (String) params.get("beginDate");
		Calendar cl0 = Calendar.getInstance();
		cl0.setFirstDayOfWeek(Calendar.MONDAY);
		cl0.setTime(sdf.parse(beginDate));
		cl0.set(Calendar.DAY_OF_WEEK, cl0.getFirstDayOfWeek());
		params.put("beginDate", sdf.format(cl0.getTime()));
        
		Calendar cl1 = Calendar.getInstance();   
		String endDate = (String) params.get("endDate");
		cl1.setFirstDayOfWeek(Calendar.MONDAY);
		cl1.setTime(sdf.parse(endDate));
		cl1.set(Calendar.DAY_OF_WEEK, cl1.getFirstDayOfWeek() + 6);
		params.put("endDate", sdf.format(cl1.getTime()));
		
		//获取需要连接的小区信息
		List<Map<String, String>> areaInfoList = getAreaInfoList(list);
		
		//获取相邻小区之间关系
		List<String> adjoinList = getAreaAdjoinList(list);
		
		//获取关联小区的code
		String areaCodeStr = getAreaCodeStr(list);
		params.put("areaCodeStr", areaCodeStr);
		
		//查询流出TOP
		List<Map<String, Object>> flowOut = psgCorridorMapper.queryFlowOutTop(params);
		
		//查询流入TOP
		List<Map<String, Object>> flowIn = psgCorridorMapper.queryFlowInTop(params);
		
		//查询客流分析数据
		List<Map<String, Object>> flowAnalysis = psgCorridorMapper.queryFlowAnalysisByWeek(params);
		
		//查询相邻小区客流情况
		Map<String, Integer> adjoinAreaFlow = queryAdjoinAreaFlow(params, adjoinList);
		
		result.put("adjoinList", adjoinList);
		result.put("areaInfoList", areaInfoList);
		result.put("flowOut", flowOut);
		result.put("flowIn", flowIn);
		result.put("flowAnalysis", flowAnalysis);
		result.put("adjoinAreaFlow", adjoinAreaFlow);
		
		return result;
	}

	/**
	 *  按月查询客运走廊数据
	 * @param params
	 * @return
	 * @throws ParseException 
	 */
	public Map<String, Object> queryPsgCorridorByMonth(Map<String, Object> params) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		String areaCode = (String) params.get("areaCode");
		if(StringUtils.isEmpty(areaCode)){
			return null;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
		String beginDate = (String) params.get("beginDate");
		Calendar cl0 = Calendar.getInstance();
		cl0.setTime(sdf.parse(beginDate));
		cl0.set(Calendar.DATE, cl0.getActualMinimum(Calendar.DATE));
		params.put("beginDate", sdf.format(cl0.getTime()));
        
		Calendar cl1 = Calendar.getInstance();   
		String endDate = (String) params.get("endDate");
		cl1.setTime(sdf.parse(endDate));
		cl1.set(Calendar.DATE, cl1.getActualMaximum(Calendar.DATE));
		params.put("endDate", sdf.format(cl1.getTime()));
		
		//小区信息
		List<Map<String, Object>> list = psgCorridorMapper.queryAreaInfo(areaCode);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		
		//获取需要连接的小区信息
		List<Map<String, String>> areaInfoList = getAreaInfoList(list);
		
		//获取相邻小区之间关系
		List<String> adjoinList = getAreaAdjoinList(list);
		
		//获取关联小区的code
		String areaCodeStr = getAreaCodeStr(list);
		params.put("areaCodeStr", areaCodeStr);
		
		//查询流出TOP
		List<Map<String, Object>> flowOut = psgCorridorMapper.queryFlowOutTop(params);
		
		//查询流入TOP
		List<Map<String, Object>> flowIn = psgCorridorMapper.queryFlowInTop(params);
		
		//查询客流分析数据
		List<Map<String, Object>> flowAnalysis = psgCorridorMapper.queryFlowAnalysisByMonth(params);
		
		//查询相邻小区客流情况
		Map<String, Integer> adjoinAreaFlow = queryAdjoinAreaFlow(params, adjoinList);
		
		result.put("adjoinList", adjoinList);
		result.put("areaInfoList", areaInfoList);
		result.put("flowOut", flowOut);
		result.put("flowIn", flowIn);
		result.put("flowAnalysis", flowAnalysis);
		result.put("adjoinAreaFlow", adjoinAreaFlow);
		
		return result;
	}

	/**
	 * 查询客流TOP情况
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryFlowTop(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		List<String> adjoinList = (List<String>) params.get("adjoin");
		Map<String, Integer> totleFlowData = new HashMap<String, Integer>();
		if(CollectionUtils.isNotEmpty(adjoinList)){
			Map<String, Integer> flowOut = new HashMap<String, Integer>();
			Map<String, Integer> flowIn = new HashMap<String, Integer>();
			for(String adjoin : adjoinList){
				String[] areaIdArr = adjoin.split(",");
				params.put("source", areaIdArr[0]);
				params.put("target", areaIdArr[1]);
				int flow = psgCorridorMapper.queryTotleAreaODByDay(params);
				totleFlowData.put(adjoin, flow);
				
				if(flowOut.containsKey(areaIdArr[0])){
					flowOut.put(areaIdArr[0], flowOut.get(areaIdArr[0])+flow);
				}else{
					flowOut.put(areaIdArr[0], flow);
				}
				
				if(flowIn.containsKey(areaIdArr[1])){
					flowOut.put(areaIdArr[1], flowOut.get(areaIdArr[1])+flow);
				}else{
					flowIn.put(areaIdArr[1], flow);
				}
			}
			
			List<Map.Entry<String, Integer>> out =
				    new ArrayList<Map.Entry<String, Integer>>(flowOut.entrySet());
			Collections.sort(out, new Comparator<Map.Entry<String, Integer>>() {   
				public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
			
			List<Map.Entry<String, Integer>> in =
				    new ArrayList<Map.Entry<String, Integer>>(flowIn.entrySet());
			Collections.sort(in, new Comparator<Map.Entry<String, Integer>>() {   
				public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});

			result.put("out", out);
			result.put("in", in);
			return result;
		}
		return result;
	}

	/*private List<String> getAreaIdList(List<String> adjoinList) {
		if(CollectionUtils.isNotEmpty(adjoinList)){
			List<String> areaIdList = new ArrayList<String>();
			for(String adjoin : adjoinList){
				String[] areaArr = adjoin.split(",");
				if(!areaIdList.contains(areaArr[0])){
					areaIdList.add(areaArr[0]);
				}
				
				if(!areaIdList.contains(areaArr[1])){
					areaIdList.add(areaArr[1]);
				}
			}
		}
		return null;
	}*/

	public Object queryFlowAnalysis(Map<String, Object> params) {
		return null;
	}

	public Object queryAreaMap(Map<String, Object> params) {
		return null;
	}

	public Object queryFlowOutTop(Map<String, Object> params) {
		return null;
	}

	public Object queryFlowInTop(Map<String, Object> params) {
		return null;
	}
}
