package com.lty.rt.assessment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.lty.rt.assessment.bean.Index;
import com.lty.rt.assessment.service.IndexService;
import com.lty.rt.assessment.vo.IndexTotalDataVo;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.basicData.treeUtil.TreeNodeUtils;
import com.lty.rt.basicData.treeUtil.TreeViewEntity;
import com.lty.rt.comm.bean.QueryCommonDto;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;

/**
 * 评分指标管理
 * @author qiq
 *
 */

@RequestMapping("/index")
@Controller
public class IndexController {
	
	private static final Logger logger  =  LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private IndexService indexService;
	
	@RequestMapping("/getLists")
	@ResponseBody
	public Response<List<Index>> getLists(@RequestBody String indexType){
		Response<List<Index>> resp = new Response<List<Index>>();
		try{
			List<Index> list = indexService.getLists(Integer.valueOf(indexType));
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("index.getLists() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/initLevel")
	@ResponseBody
	public Response<Index> initLevel(){
		Response<Index> resp = new Response<Index>();
		try{
			Index index = indexService.initLevel();
			resp.setData(index);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("index.initLevel() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/insert")
	@ResponseBody
	public Response<Integer> insert(Index index){
		Response<Integer> resp = new Response<Integer>();
		try{
			Integer count = indexService.saveOrUpdate(index);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("index.insert() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
		
	}
	
	@RequestMapping("/getIndexTree")
	@ResponseBody
	public Response<JSONArray> getIndexTree(@RequestBody String indexType) {
		Response<JSONArray> resp = new Response<JSONArray>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("indexType", indexType);
			List<TreeNode> nodeList = indexService.getIndexTree(map);
			List<TreeViewEntity> treeList = TreeNodeUtils.generateTreeViewEntity(nodeList);
			resp.setData((JSONArray) JSONArray.toJSON(treeList));
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("index.getIndexTree() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/delByIds")
	@ResponseBody
	public Response<Integer> delByIds(@RequestBody String dataIds){
		Response<Integer> resp = new Response<Integer>();
		try{
			Integer count = indexService.delByIds(dataIds);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (ApplicationException el) {
			logger.error("level.delByIds() error{}",el);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(el.getMessage());
		}catch (Exception e) {
			logger.error("index.delByIds() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/getIndexById")
	@ResponseBody
	public Response<Index> getIndexById(@RequestBody String dataId){
		Response<Index> resp = new Response<Index>();
		try{
			Index index = indexService.getIndexById(dataId);
			resp.setData(index);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("index.getIndexById() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

	@RequestMapping("/getAllTopLevels")
	@ResponseBody
	public Response<List<Index>> getAllTopLevels(@RequestBody String indexType){
		Response<List<Index>> resp = new Response<List<Index>>();
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("parentId", "-1");
			map.put("indexType", Integer.valueOf(indexType));
			List<Index> list = indexService.getListByMap(map);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("index.getLists() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/getTotalChartData")
	@ResponseBody
	public Response<List<Object>> getTotalChartData(QueryCommonDto dto){
		Response<List<Object>> resp = new Response<List<Object>>();
		try{
			List<Object> list = new ArrayList<Object>();
			Map<String, List<IndexTotalDataVo>> resultMap = indexService.getTotalChartData(dto);
			Map<String, Object> map = indexService.getTotalAvgData(dto, resultMap);
			list.add(resultMap);
			list.add(map);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("index.getTotalChartData() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/getDetailChartData")
	@ResponseBody
	public Response<Map<String, List<IndexTotalDataVo>>> getDetailChartData(QueryCommonDto dto){
		Response<Map<String, List<IndexTotalDataVo>>> resp = new Response<Map<String, List<IndexTotalDataVo>>>();
		try{
			Map<String, List<IndexTotalDataVo>> resultMap = indexService.getDetailChartData(dto);
			resp.setData(resultMap);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("index.getTotalChartData() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
}
