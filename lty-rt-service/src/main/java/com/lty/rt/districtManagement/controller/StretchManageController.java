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
import com.lty.rt.basicData.bean.Stretch;
import com.lty.rt.basicData.service.StretchService;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.basicData.treeUtil.TreeNodeUtils;
import com.lty.rt.basicData.treeUtil.TreeViewEntity;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;

/**
 * 路段管理
 * @author qiq
 *
 */
@RequestMapping("/stretchManagement")
@Controller
public class StretchManageController {
	
	private static final Logger logger  =  LoggerFactory.getLogger(AreaMapController.class);
	
	@Autowired
	private StretchService stretchService;

	@RequestMapping("/getStretchList")
	@ResponseBody
	public Response<List<Stretch>> getStretchList(@RequestBody Map<String, Object> map) {
		Response<List<Stretch>> resp = new Response<List<Stretch>>();
		try{
			List<Stretch> list =  stretchService.findListByMap(map);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("StretchManageController.getStretchList() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		
		return resp;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/getDistrictTree")
	@ResponseBody
	public Response<JSONArray> getDistrictTree() {
		Response<JSONArray> resp = new  Response<JSONArray>();
		try{
			List<TreeNode> nodeList = stretchService.getStretchTree(new HashMap());
			List<TreeViewEntity> treeList = TreeNodeUtils.generateTreeViewEntity(nodeList);
			resp.setData((JSONArray) JSONArray.toJSON(treeList));
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("StretchManageController.getDistrictTree() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
		
	}
	
	@RequestMapping("/delById")
	@ResponseBody
	public Response<Integer> delById(@RequestBody String id){
		Response<Integer> resp = new Response<Integer>();
		try{
			if(StringUtils.isBlank(id)){
				resp.setCode(ReturnCode.ERROR_03.getCode());
				resp.setMsg(ReturnCode.ERROR_03.getMsg());
			}else{
				int count = stretchService.batchDeleteByPrimaryKey(id.split(","));
				resp.setData(count);
				resp.setCode(ReturnCode.SUCCESS.getCode());
				resp.setMsg(ReturnCode.SUCCESS.getMsg());
			}
		} catch (Exception e) {
			logger.error("StretchManageController.delById() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	

	@RequestMapping("/getStretchByPrimaryKey")
	@ResponseBody
	public Response<Stretch> getStretchByPrimaryKey(@RequestBody String id){
		Response<Stretch> resp = new Response<Stretch>();
		try{
			Stretch stretch = stretchService.selectByPrimaryKey(id);
			resp.setData(stretch);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("StretchManageController.getStretchByPrimaryKey() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/insertStretch")
	@ResponseBody
	public Response<Integer> insertStretch(Stretch stretch){
		Response<Integer> resp = new Response<Integer>();
		try{
			int count = stretchService.insertStretch(stretch);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("StretchManageController.insertStretch() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
}
