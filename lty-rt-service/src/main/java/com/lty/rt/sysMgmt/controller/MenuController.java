package com.lty.rt.sysMgmt.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.basicData.treeUtil.TreeNodeUtils;
import com.lty.rt.basicData.treeUtil.TreeViewEntity;
import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;
import com.lty.rt.sysMgmt.entity.Menu;
import com.lty.rt.sysMgmt.service.MenuService;

@Controller
@RequestMapping(value="/menu")
public class MenuController {
	private static final Logger logger  =  LoggerFactory.getLogger(MenuController.class);
	@Autowired
	private MenuService menuService;
	/**
	 * 获取菜单树
	 * @return
	 */
	@RequestMapping("/getMenuTree")
	@ResponseBody
	public Response<JSONArray> getMenuTree() {
		Response<JSONArray> resp = new Response<JSONArray>();
		try{
			List<TreeNode> nodeList = menuService.getMenuTree(new HashMap<String, Object>());
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
	
	
	/**
	 * 保存菜单信息
	 * @param menu
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public RTResponse save(Menu menu){
		menuService.saveMenu(menu);
		RTResponse resp =new RTResponse(); 
		return resp;
	}
	

	/**
	 * 删除菜单
	 * @param menuId
	 * @param out
	 */
	@RequestMapping(value="/del")
	@ResponseBody
	public RTResponse delete(@RequestBody Integer menuId){
		menuService.deleteMenuById(menuId);
		RTResponse resp =new RTResponse(); 
		return resp;
	}
}
