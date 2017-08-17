package com.lty.rt.sysMgmt.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.sysMgmt.entity.Menu;

public interface MenuMapper {
	List<Menu> listAllParentMenu();
	List<Menu> listSubMenuByParentId(Integer parentId);
	Menu getMenuById(Integer menuId);
	int insertMenu(Menu menu);
	int updateMenu(Menu menu);
	int deleteMenuById(Integer menuId);
	List<Menu> listAllSubMenu();
	List<TreeNode> getMenuTree(Map<String,Object> map);
	Integer getMaxMenuId(Integer parentId);
}
