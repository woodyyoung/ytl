package com.lty.rt.sysMgmt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lty.rt.sysMgmt.entity.Role;

public interface RoleMapper {
	List<Role> listAllRoles();
	Role getRoleById(int roleId);
	void insertRole(Role role);
	void updateRoleBaseInfo(Role role);
	void deleteRoleById(int roleId);
	int getCountByName(Role role);
	void updateRoleRights(Role role);
	int getMaxRoleId();
	int insertRoleMenuRefrence(@Param("roleId") int roleId,@Param("menuId") int menuId);
	int deleteRoleMenuRefrenceByRowId(@Param("roleId") int roleId);
}
