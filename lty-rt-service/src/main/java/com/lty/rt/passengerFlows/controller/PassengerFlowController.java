package com.lty.rt.passengerFlows.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.basicData.bean.Line;
import com.lty.rt.basicData.bean.PlatForm;
import com.lty.rt.basicData.service.PlatFormFlowService;
import com.lty.rt.districtManagement.service.StretchPlatformService;
import com.lty.rt.passengerFlows.bean.PlatFormAndLine;
import com.lty.rt.passengerFlows.bean.StretchPlatFormLine;
import com.lty.rt.passengerFlows.service.PassengerFlowService;
import com.lty.rt.utils.CopyUtils;

@RequestMapping("/myPassengerFlow")
@Controller
public class PassengerFlowController {

	@Autowired
	private PassengerFlowService myPassengerFlowService;
	@Autowired
	private PlatFormFlowService platFormFlowService;
	@Autowired
	private StretchPlatformService stretchPlatformService;
	
	//缓存站台下拉框
	public static List<StretchPlatFormLine> stationList =new ArrayList<StretchPlatFormLine>();

	@RequestMapping("/getPlatFormList")
	@ResponseBody
	public List<PlatForm> getPlatFormList(@RequestBody Map<String, Object> map) {
		List<PlatForm> list = new ArrayList<PlatForm>();
		list = platFormFlowService.findListByMap(map);
		return list;
	}

	@RequestMapping("/selectStretchAndPlat")
	@ResponseBody
	public List<StretchPlatFormLine> selectStretchAndPlat(@RequestBody Map<String, Object> map) {
		String keywords = (String)map.get("keywords");
		if(!stationList.isEmpty()){
			return filterStation(keywords, stationList);
		}
		List<StretchPlatFormLine> list = new ArrayList<StretchPlatFormLine>();
		list = stretchPlatformService.selectStretchAndPlat("");
		if(list!=null&&!list.isEmpty()){
			stationList  = list	; 
		}
		return filterStation(keywords, list);
	}
	
	private List<StretchPlatFormLine> filterStation(String keywords,List<StretchPlatFormLine> sourceList){
		if(StringUtils.isBlank(keywords)){
			return sourceList;
		}
		if(sourceList==null||sourceList.isEmpty()){
			return sourceList;
		}
		
		List<StretchPlatFormLine> stationList = CopyUtils.deepCopyList(sourceList);
		List<StretchPlatFormLine> resultStationList = new ArrayList<StretchPlatFormLine>();
		
		for(StretchPlatFormLine stretch:stationList){
			boolean flag = false;
			if(stretch.getName().indexOf(keywords)!=-1){
				resultStationList.add(stretch);
				continue;
			}
			List<PlatFormAndLine> platformList = stretch.getList();
			List<PlatFormAndLine> resultPlatformList = new ArrayList<PlatFormAndLine>();
			if(platformList==null||platformList.isEmpty()){
				continue;
			}
			
			for(PlatFormAndLine platform: platformList){
				 boolean lineFlag = false;
				 if(platform.getName().indexOf(keywords)!=-1){
					 resultPlatformList.add(platform);
					 flag = true;
					 continue;
				 }
				 
				 List<Line> lineList = platform.getList();
				 List<Line> resultLineList = new ArrayList<Line>();
				 if(lineList==null||lineList.isEmpty()){
					continue;
				 }
				 
				 for(Line line:lineList){
					 if(line.getName().indexOf(keywords)!=-1){
						 lineFlag = true;
						 flag = true;
						 resultLineList.add(line);
					 }
				 }
				 
				 platform.setList(resultLineList);
				 
				 if(lineFlag){
					 resultPlatformList.add(platform);
				 }
			}
			
			stretch.setList(resultPlatformList);
			
			if(flag){
				resultStationList.add(stretch);
			}
			
		}
		return resultStationList;
	}
	

	@RequestMapping("/getPassengerData")
	@ResponseBody
	@Transactional
	public List<Map<String, Object>> getPassengerData(@RequestBody Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String startTime = (String) map.get("startTime");
		String endTime = (String) map.get("endTime");
		try {
			if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
				list = myPassengerFlowService.findListByMap(map);
				/*
				 * List<Map<String, Object>> GPSCorrectlist = new
				 * ArrayList<Map<String, Object>>(); for (Map<String, Object> m
				 * : list) { double lat = ((BigDecimal)
				 * m.get("LATITUDE")).doubleValue(); double lng = ((BigDecimal)
				 * m.get("LONGITUDE")).doubleValue(); double[] latlon =
				 * GpsCorrect.gcjDecrypt(lat, lng);// 站点坐标纠偏 m.put("LATITUDE",
				 * StringUtils.isBlank(String.valueOf(latlon[0])) ? "" :
				 * String.valueOf(latlon[0])); m.put("LONGITUDE",
				 * StringUtils.isBlank(String.valueOf(latlon[1])) ? "" :
				 * String.valueOf(latlon[1]));
				 * 
				 * double lat = ((BigDecimal) m.get("LATITUDE")).doubleValue();
				 * double lng = ((BigDecimal) m.get("LONGITUDE")).doubleValue();
				 * double[] latlon = GpsCorrect.gcjDecrypt(lat, lng);// 站点坐标纠偏
				 * m.put("LATITUDE",
				 * StringUtils.isBlank(String.valueOf(latlon[0])) ? "" :
				 * String.valueOf(latlon[0])); m.put("LONGITUDE",
				 * StringUtils.isBlank(String.valueOf(latlon[1])) ? "" :
				 * String.valueOf(latlon[1]));
				 * 
				 * GPSCorrectlist.add(m); } list = GPSCorrectlist;
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
