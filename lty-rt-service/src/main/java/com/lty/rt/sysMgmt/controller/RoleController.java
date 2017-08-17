package com.lty.rt.sysMgmt.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.sysMgmt.entity.Role;
import com.lty.rt.sysMgmt.service.MenuService;
import com.lty.rt.sysMgmt.service.RoleService;

@Controller
@RequestMapping(value="/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	/**
	 * 显示角色列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/listAllRole")
	@ResponseBody
	public RTResponse list(){
		RTResponse resp = new RTResponse();
		List<Role> roleList = roleService.listAllRoles();
		resp.setData(roleList);
		return resp;
	}
	
	/**
	 * 保存角色信息
	 * @param out
	 * @param role
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public RTResponse save(Role role){
		if(role.getRoleId()!=null && role.getRoleId().intValue()>0){
			roleService.updateRoleBaseInfo(role);
		}else{
			roleService.insertRole(role);
		}
		RTResponse resp = new RTResponse();
		return resp;
	}
	
	/**
	 * 请求角色授权页面
	 * @param roleId
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value="/auth")
	public String auth(@RequestParam int roleId,Model model){
		List<Menu> menuList = menuService.listAllMenu();
		Role role = roleService.getRoleById(roleId);
		String roleRights = role.getRights();
		if(Tools.notEmpty(roleRights)){
			for(Menu menu : menuList){
				menu.setHasMenu(RightsHelper.testRights(roleRights, menu.getMenuId()));
				if(menu.isHasMenu()){
					List<Menu> subMenuList = menu.getSubMenu();
					for(Menu sub : subMenuList){
						sub.setHasMenu(RightsHelper.testRights(roleRights, sub.getMenuId()));
					}
				}
			}
		}
		JSONArray arr = JSONArray.fromObject(menuList);
		String json = arr.toString();
		json = json.replaceAll("menuId", "id").replaceAll("menuName", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
		model.addAttribute("zTreeNodes", json);
		model.addAttribute("roleId", roleId);
		return "authorization";
	}*/
	
	/**
	 * 保存角色权限
	 * @param roleId
	 * @param menuIds
	 * @param out
	 */
	@RequestMapping(value="/auth/save")
	@ResponseBody
	public RTResponse saveAuth(@RequestBody int roleId,@RequestBody String menuIds){
		BigInteger rights = null;
		Role role = roleService.getRoleById(roleId);
		role.setRights(rights.toString());
		roleService.updateRoleRights(role);
		RTResponse resp = new RTResponse();
		return resp;
	}
	
	/**
	 * 保存角色权限
	 * @param roleId
	 * @param menuIds
	 * @param out
	 */
	@RequestMapping(value="/grantMenu")
	@ResponseBody
	public RTResponse grantMenu(@RequestBody Map<String, Object> params){
		int  roleId = (Integer)params.get("roleId");
		List<Integer>  menuIds = (List<Integer>)params.get("menuIds");
		roleService.grantMenu(roleId, menuIds);
		RTResponse resp = new RTResponse();
		return resp;
	}
}
