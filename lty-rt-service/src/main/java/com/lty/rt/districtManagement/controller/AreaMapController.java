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
import com.lty.rt.districtManagement.bean.AreaMap;
import com.lty.rt.districtManagement.service.AreaMapService;

/**
 * 区域地图
 * @author qiq
 *
 */
@RequestMapping("/areaMap")
@Controller
public class AreaMapController {
	
	private static final Logger logger  =  LoggerFactory.getLogger(AreaMapController.class);

	
	@Autowired
	private AreaMapService areaMapService;

	@SuppressWarnings("unchecked")
	@RequestMapping("/insertAreaMap")
	@ResponseBody
	public Response<Integer> insertAreaMap(@RequestBody Map<String, Object> map){
		Response<Integer> resp = new Response<Integer>();
		try{
			String areaId = (String)map.get("areaId");
			List<Map<String, Object>> mapList = (List<Map<String, Object>>)map.get("oppath");
			
			List<AreaMap> areaMapList = new ArrayList<AreaMap>();
			int count = 0;
			if(mapList != null && mapList.size() > 0){
				for(int i = 0; i < mapList.size(); i++){
					AreaMap areaMap = new AreaMap();
					areaMap.setAreaid(areaId);
					areaMap.setLat((Double)mapList.get(i).get("lat"));
					areaMap.setIng((Double)mapList.get(i).get("lng"));
					areaMap.setOrderby(i);
					areaMapList.add(areaMap);
				}
				count = areaMapService.batchInsert(areaMapList);
				resp.setData(count);
				resp.setCode(ReturnCode.SUCCESS.getCode());
				resp.setMsg(ReturnCode.SUCCESS.getMsg());
			}else{
				if(StringUtils.isBlank(areaId)){
					resp.setData(count);
					resp.setCode(ReturnCode.ERROR_02.getCode());
					resp.setMsg(ReturnCode.ERROR_02.getMsg());
				}else{
					count = areaMapService.delByAreaid(areaId);
					resp.setData(count);
					resp.setCode(ReturnCode.SUCCESS.getCode());
					resp.setMsg(ReturnCode.SUCCESS.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error("areaMap.insertAreaMap() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

	@RequestMapping("/selectAreaMapByAreaId")
	@ResponseBody
	public Response<List<AreaMap>> selectAreaMapByAreaId(@RequestBody String areaId){
		Response<List<AreaMap>> resp = new Response<List<AreaMap>>();
		try{
			List<AreaMap> list = areaMapService.selectAreaMapByAreaId(areaId);
		
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (ApplicationException ex){
			logger.error("areaMap.selectAreaMapByAreaId() error:{}",ex);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		} catch (Exception e) {
			logger.error("areaMap.selectAreaMapByAreaId() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	
}
