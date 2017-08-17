package com.lty.rt.sysMgmt.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lty.rt.basicData.treeUtil.State;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.basicData.treeUtil.TreeNodeUtils;
import com.lty.rt.basicData.treeUtil.TreeViewEntity;

public class Role {
	private Integer roleId;
	private String roleName;
	private String rights;
	private List<Menu> menus;
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRights() {
		return rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}
	
	public List<TreeViewEntity> buildTreeViewEntity(){
		if(this.menus==null||this.menus.isEmpty()){
			return null;
		}
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		for(Menu menu:this.menus){
			TreeNode node = new TreeNode();
			node.setId(String.valueOf(menu.getMenuId()));
			node.setNodeName(menu.getMenuName());
			node.setPid(String.valueOf(menu.getParentId()));
			node.setArg(menu.getMenuUrl());
			nodeList.add(node);
		}
		List<TreeViewEntity> treeList = TreeNodeUtils.generateTreeViewEntity(nodeList);
		State s = new State();
		s.setChecked(true);
		for(TreeViewEntity treeView:treeList){
			treeView.setState(s);
		}
		return treeList;
	}
	
}
