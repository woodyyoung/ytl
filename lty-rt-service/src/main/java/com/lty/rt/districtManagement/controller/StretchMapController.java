package com.lty.rt.districtManagement.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;
import com.lty.rt.districtManagement.bean.StretchMap;
import com.lty.rt.districtManagement.service.StretchMapService;

/**
 * 路段地图
 * @author qiq
 *
 */
@RequestMapping("/stretchMap")
@Controller
public class StretchMapController {
	
	private static final Logger logger  =  LoggerFactory.getLogger(StretchMapController.class);

	
	@Autowired
	private StretchMapService stretchMapService;

	@SuppressWarnings("unchecked")
	@RequestMapping("/insertStretchMap")
	@ResponseBody
	public Response<Integer> insertStretchMap(@RequestBody Map<String, Object> map){
		Response<Integer> resp = new Response<Integer>();
		try{
			String stretchId = (String)map.get("stretchId");
			List<Map<String, Object>> mapList = (List<Map<String, Object>>)map.get("oppath");
			
			List<StretchMap> stretchMapList = new ArrayList<StretchMap>();
			int count = 0;
			if(mapList != null && mapList.size() > 0){
				for(int i = 0; i < mapList.size(); i++){
					StretchMap stretchMap = new StretchMap();
					stretchMap.setStretchid(stretchId);
					stretchMap.setLat((Double)mapList.get(i).get("lat"));
					stretchMap.setIng((Double)mapList.get(i).get("lng"));
					stretchMap.setOrderby(i);
					stretchMapList.add(stretchMap);
				}
				 count = stretchMapService.batchInsert(stretchMapList);
				 resp.setData(count);
				 resp.setCode(ReturnCode.SUCCESS.getCode());
				 resp.setMsg(ReturnCode.SUCCESS.getMsg());
			}else {
				if(StringUtils.isBlank(stretchId)){
					resp.setData(count);
					resp.setCode(ReturnCode.ERROR_02.getCode());
					resp.setMsg(ReturnCode.ERROR_02.getMsg());
				}else{
					count = stretchMapService.delByStretchid(stretchId);
					resp.setData(count);
					resp.setCode(ReturnCode.SUCCESS.getCode());
					resp.setMsg(ReturnCode.SUCCESS.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error("stretchMap.insertStretchMap() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

	@RequestMapping("/selectStretchMapByStretchId")
	@ResponseBody
	public Response<List<StretchMap>> selectStretchMapByStretchId(@RequestBody String stretchId){
		Response<List<StretchMap>> resp = new Response<List<StretchMap>>();
		try{
			List<StretchMap> list = stretchMapService.selectStretchMapByStretchId(stretchId);
		
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (ApplicationException ex){
			logger.error("stretchMap.selectStretchMapByStretchId() error:{}",ex);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		} catch (Exception e) {
			logger.error("stretchMap.selectStretchMapByStretchId() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	
}
