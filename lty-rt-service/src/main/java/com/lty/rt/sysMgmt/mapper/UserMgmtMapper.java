package com.lty.rt.sysMgmt.mapper;

import java.util.List;
import java.util.Map;

import com.lty.rt.sysMgmt.entity.User;

/**
 * 用户管理 数据库接口
 * @author yyw
 *
 */
public interface UserMgmtMapper {

	/**
	 * 查询用户
	 * @param params
	 * @return
	 */
	List<User> queryUser(Map<String, Object> params);

	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	int insertUser(User user);

	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	int updateUser(User user);

	
	/**
	 * 输出用户
	 * @param ids 支持参数格式  'id1', 'id2', 'id3' ,...
	 * @return
	 */
	int delUser(String[] ids);

}
