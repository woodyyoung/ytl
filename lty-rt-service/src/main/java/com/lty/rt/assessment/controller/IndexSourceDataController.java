package com.lty.rt.assessment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.assessment.bean.Index;
import com.lty.rt.assessment.service.IndexSourceDataService;
import com.lty.rt.assessment.vo.IndexSourceDataVo;
import com.lty.rt.comm.bean.QueryCommonDto;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;

/**
 * 分项评价计算
 * 
 * @author qiq
 *
 */

@RequestMapping("/indexSourceData")
@Controller
public class IndexSourceDataController {

	private static final Logger logger = LoggerFactory.getLogger(IndexSourceDataController.class);

	@Autowired
	private IndexSourceDataService indexSourceDataService;

	@RequestMapping("/getChartData")
	@ResponseBody
	public Response<List<Object>> getLists(QueryCommonDto dto) {
		Response<List<Object>> resp = new Response<List<Object>>();
		try {
			List<Object> list = new ArrayList<Object>();
			Map<String, List<IndexSourceDataVo>> resultMap = indexSourceDataService.getChartData(dto);
			Map<String, Object> map = indexSourceDataService.getAvgData(dto, resultMap);
			list.add(resultMap);
			list.add(map);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("indexSourceData.getChartData() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

	@RequestMapping("/getDescription")
	@ResponseBody
	public Response<Index> getDescription(@RequestBody Map<String, Object> map) {
		Response<Index> resp = new Response<Index>();
		try {
			String id = map.get("id") == null ? "" : map.get("id").toString();
			if (StringUtils.isNotBlank(id)) {
				Index index = indexSourceDataService.getIndexByID(id);
				resp.setData(index);
			}
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("indexSourceData.getChartData() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/getScoresList")
	@ResponseBody
	public Response<List<IndexSourceDataVo>> getScoresList(@RequestBody Map<String, Object> map){
		Response<List<IndexSourceDataVo>> resp = new Response<List<IndexSourceDataVo>>();
		try {
			String id = map.get("arg") == null ? "" : map.get("arg").toString();
			if (StringUtils.isNotBlank(id)) {
				map.put("arg", id);
				List<IndexSourceDataVo> list = indexSourceDataService.getScoresListByIndexId(map);
				resp.setData(list);
			}
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("indexSourceData.getChartData() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	
	@RequestMapping("/searchIndexData")
	@ResponseBody
	public Response<List<Map<String,Object>>> searchIndexData(@RequestBody Map<String, Object> map){
		Response<List<Map<String,Object>>> resp = new Response<List<Map<String,Object>>>();
		try {
			List<Map<String,Object>> list = indexSourceDataService.searchIndexData(map);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("indexSourceData.getChartData() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

	

	@RequestMapping("/save")
	@ResponseBody
	public Response<Integer> save(@RequestBody Map<String, Object> map) {
		Response<Integer> resp = new Response<Integer>();
		try {
			Integer count = indexSourceDataService.saveOrUpdate(map);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("indexSourceData.save() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public Response<Integer> update(@RequestBody Map<String, Object> map){
		Response<Integer> resp = new Response<Integer>();
		try {
			Integer count = indexSourceDataService.update(map);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("indexSourceData.update() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/updateIndexData")
	@ResponseBody
	public Response<Integer> updateIndexData(@RequestBody Map<String, Object> map){
		Response<Integer> resp = new Response<Integer>();
		try {
			Integer count = indexSourceDataService.updateIndexData(map);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("indexSourceData.update() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/delByIndexIdAndDate")
	@ResponseBody
	public Response<Integer> delByIndexIdAndDate(@RequestBody String data){
		Response<Integer> resp = new Response<Integer>();
		try {
			if(StringUtils.isNotBlank(data)){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ids", data.split(","));
				Integer count = indexSourceDataService.delByIndexIdAndDate(map);
				resp.setData(count);
			}
			
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("indexSourceData.update() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

}
