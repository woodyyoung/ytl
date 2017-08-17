package com.lty.rt.sysMgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.sysMgmt.entity.Role;
import com.lty.rt.sysMgmt.mapper.RoleMapper;
@Service
public class RoleService {
	@Autowired
	private RoleMapper roleMapper;
	
	@Transactional
	public void grantMenu(int roleId,List<Integer>menuIds){
		//删除之前的授权关系
		roleMapper.deleteRoleMenuRefrenceByRowId(roleId);
		for(int menuId:menuIds){
			roleMapper.insertRoleMenuRefrence(roleId, menuId);
		}
	}
	
	public List<Role> listAllRoles() {
		// TODO Auto-generated method stub
		return roleMapper.listAllRoles();
	}

	public void deleteRoleById(int roleId) {
		// TODO Auto-generated method stub
		roleMapper.deleteRoleById(roleId);
	}

	public Role getRoleById(int roleId) {
		// TODO Auto-generated method stub
		return roleMapper.getRoleById(roleId);
	}

	public boolean insertRole(Role role) {
		// TODO Auto-generated method stub
		if(roleMapper.getCountByName(role)>0)
			return false;
		else{
			int roleId = roleMapper.getMaxRoleId();
			role.setRoleId(roleId);
			roleMapper.insertRole(role);
			return true;
		}
	}

	public boolean updateRoleBaseInfo(Role role) {
		// TODO Auto-generated method stub
		if(roleMapper.getCountByName(role)>0)
			return false;
		else{
			roleMapper.updateRoleBaseInfo(role);
			return true;
		}
	}
	
	public void updateRoleRights(Role role) {
		// TODO Auto-generated method stub
		roleMapper.updateRoleRights(role);
	}

	public RoleMapper getRoleMapper() {
		return roleMapper;
	}

	public void setRoleMapper(RoleMapper roleMapper) {
		this.roleMapper = roleMapper;
	}

}
