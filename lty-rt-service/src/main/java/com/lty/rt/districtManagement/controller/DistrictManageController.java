package com.lty.rt.districtManagement.controller;

import java.util.HashMap;
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

import com.alibaba.fastjson.JSONArray;
import com.lty.rt.basicData.bean.PjmkArea;
import com.lty.rt.basicData.service.PjmkAreaService;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.basicData.treeUtil.TreeNodeUtils;
import com.lty.rt.basicData.treeUtil.TreeViewEntity;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;

/**
 * 区域管理
 * @author qiq
 *
 */
@RequestMapping("/districtManagement")
@Controller
public class DistrictManageController {
	
	private static final Logger logger  =  LoggerFactory.getLogger(DistrictManageController.class);

	
	@Autowired
	private PjmkAreaService pjmkAreaService;

	
	@RequestMapping("/getDistrictList")
	@ResponseBody
	public Response<List<PjmkArea>> getDistrictList(@RequestBody Map<String, Object> map) {
		Response<List<PjmkArea>> resp = new Response<List<PjmkArea>>();
		try{
			List<PjmkArea> list = pjmkAreaService.findListByMap(map);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("districtManagement.getDistrictList() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/delById")
	@ResponseBody
	public Response<Integer> delById(@RequestBody String id){
		Response<Integer>  resp = new Response<Integer> ();
		try{
			if(StringUtils.isBlank(id)){
				resp.setCode(ReturnCode.ERROR_03.getCode());
				resp.setMsg(ReturnCode.ERROR_03.getMsg());
			}else{
				Integer count = pjmkAreaService.deleteByAreaCode(id);
				resp.setData(count);
				resp.setCode(ReturnCode.SUCCESS.getCode());
				resp.setMsg(ReturnCode.SUCCESS.getMsg());
			}
		} catch (ApplicationException e) {
			logger.error("districtManagement.delById() error{}",e);
			resp.setCode(e.getCode());
			resp.setMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("districtManagement.delById() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/getDistrictTree")
	@ResponseBody
	public Response<JSONArray> getDistrictTree() {
		Response<JSONArray> resp = new Response<JSONArray>();
		try{
			List<TreeNode> nodeList = pjmkAreaService.getDistrictTree(new HashMap());
			List<TreeViewEntity> treeList = TreeNodeUtils.generateTreeViewEntity(nodeList);
			resp.setData((JSONArray) JSONArray.toJSON(treeList));
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("districtManagement.getDistrictTree() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/getPjmkAreaByPrimaryKey")
	@ResponseBody
	public Response<PjmkArea> getPjmkAreaByPrimaryKey(@RequestBody String id){
		Response<PjmkArea> resp = new Response<PjmkArea>();
		try{
			PjmkArea pjmkArea = pjmkAreaService.selectByPrimaryKey(id);
			resp.setData(pjmkArea);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("districtManagement.getPjmkAreaByPrimaryKey() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/insertPjmkArea")
	@ResponseBody
	public Response<Integer> insertPjmkArea(PjmkArea area){
		Response<Integer> resp = new Response<Integer>();
		try{
			int count = pjmkAreaService.insertPjmkArea(area);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("districtManagement.insertPjmkArea() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/saveAreaInfo")
	@ResponseBody
	public Response<Integer> saveAreaInfo(@RequestBody Map<String, Object> dataMap){
		Response<Integer> resp = new Response<Integer>();
		try{
			int count = pjmkAreaService.saveAreaInfo(dataMap);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("districtManagement.saveAreaInfo() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
}
