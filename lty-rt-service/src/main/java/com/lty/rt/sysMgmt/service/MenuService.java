package com.lty.rt.sysMgmt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.sysMgmt.entity.Menu;
import com.lty.rt.sysMgmt.mapper.MenuMapper;

@Service
public class MenuService {
	@Autowired
	private MenuMapper menuMapper;
	
	public void deleteMenuById(Integer menuId) {
		List<Menu> listSubMenu = menuMapper.listSubMenuByParentId(menuId);
		if(listSubMenu!=null&&listSubMenu.size()>0){
			throw new ApplicationException(3020,"有子菜单，请先删除子菜单");
		}
		// TODO Auto-generated method stub
		menuMapper.deleteMenuById(menuId);
	}

	public Menu getMenuById(Integer menuId) {
		// TODO Auto-generated method stub
		return menuMapper.getMenuById(menuId);
	}

	public List<Menu> listAllParentMenu() {
		// TODO Auto-generated method stub
		return menuMapper.listAllParentMenu();
	}

	public void saveMenu(Menu menu) {
		// TODO Auto-generated method stub
		if(menu.getMenuId()!=null && menu.getMenuId().intValue()>0){
			menuMapper.updateMenu(menu);
		}else{
			Integer parentId = menu.getParentId();
			if(parentId==null){
				parentId = -1;
				menu.setParentId(-1);
			}
			Integer maxMenuId = menuMapper.getMaxMenuId(parentId);
			if(parentId==-1){
				menu.setMenuId(maxMenuId==null?1:maxMenuId);
			}else{
				menu.setMenuId(maxMenuId==null?Integer.parseInt(parentId+"01"):maxMenuId);
			}
			menuMapper.insertMenu(menu);
		}
	}

	public List<Menu> listSubMenuByParentId(Integer parentId) {
		// TODO Auto-generated method stub
		return menuMapper.listSubMenuByParentId(parentId);
	}
	
	public List<Menu> listAllMenu() {
		// TODO Auto-generated method stub
		List<Menu> rl = this.listAllParentMenu();
		for(Menu menu : rl){
			List<Menu> subList = this.listSubMenuByParentId(menu.getMenuId());
			menu.setSubMenu(subList);
		}
		return rl;
	}

	public List<Menu> listAllSubMenu(){
		return menuMapper.listAllSubMenu();
	}
	
	public MenuMapper getMenuMapper() {
		return menuMapper;
	}

	public void setMenuMapper(MenuMapper menuMapper) {
		this.menuMapper = menuMapper;
	}
	
	public List<TreeNode> getMenuTree(Map<String,Object> map){
		return menuMapper.getMenuTree(map);
	}

}
