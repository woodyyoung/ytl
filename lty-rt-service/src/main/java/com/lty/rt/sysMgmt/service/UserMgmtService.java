package com.lty.rt.sysMgmt.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.lty.rt.comm.util.UUIDUtils;
import com.lty.rt.sysMgmt.entity.User;
import com.lty.rt.sysMgmt.mapper.UserMgmtMapper;
import com.lty.rt.sysMgmt.utils.MD5Utils;

/**
 * 用户管理 业务层
 * @author yyw
 *
 */
@Service
public class UserMgmtService {
	
	private String password = "888888";

	@Autowired
	private UserMgmtMapper userMgmtMapper;
	
	/**
	 * 查询所有用户
	 * @return 
	 */
	public List<User> queryAllUser() {
		return  userMgmtMapper.queryUser(null);
	}

	/**
	 * 根据ID查询用户
	 * @param id
	 * @return
	 */
	public User queryUserById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("id", id);
		List<User> userList = userMgmtMapper.queryUser(params);
		if(CollectionUtils.isEmpty(userList)){
			return null;
		}else{
			User user = userList.get(0);
			return user;
		}
	}

	/**
	 * 保存用户 ： 新增或者修改
	 * @param user
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public int saveUser(User user){
		if(user == null){
			return 0;
		}
		

		/*//密码处理
		try {
			user.setPassword(MD5Utils.getEncryptedPwd(password));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}*/
		
		//进行保存业务
		String id = user.getId();
		if(StringUtils.isBlank(id)){
			id = UUIDUtils.getId();
			user.setId(id);
			user.setCreateDate(new Date(System.currentTimeMillis()));
			//密码处理
			try {
				user.setPassword(MD5Utils.getEncryptedPwd(password));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return userMgmtMapper.insertUser(user);
		}else{
			if(StringUtils.isNotBlank(user.getPassword())){
				try {
					user.setPassword(MD5Utils.getEncryptedPwd(user.getPassword()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
			user.setModifyDate(new Date(System.currentTimeMillis()));
			return userMgmtMapper.updateUser(user);
		}
	}
	
	/**
	 * 修改用户的密码
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public int updateUser(User user, String oldPwd)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// 验证就旧的密码与当前密码是否一致
		Map<String, Object> params = new HashMap<>();
		params.put("id", user.getId());
		List<User> userList = userMgmtMapper.queryUser(params);
		if (userList != null && userList.size() == 1) {
			User userObj = userList.get(0);
			// 加密密码进行对比
			if (MD5Utils.validPassword(oldPwd, userObj.getPassword())) {
				user.setModifyDate(new Date(System.currentTimeMillis()));
				return userMgmtMapper.updateUser(user);
			}
			return -1;
		}

		return 0;
	}
	
	/**
	 * 根据ID删除用户
	 *  支持批量删除 ID支持  "id1,id2,id1,..."
	 * @param idStr
	 * @return
	 */
	public int delUser(String idStr) {
		if(StringUtils.isBlank(idStr)){
			return 0;
		}
		
		String[] idArr = idStr.split(",");
		return userMgmtMapper.delUser(idArr);
	}

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	public User queryUserByName(String username) {
		if(StringUtils.isBlank(username)){
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("username", username);
		List<User> userList = userMgmtMapper.queryUser(params);
		if(CollectionUtils.isEmpty(userList)){
			return null;
		}else{
			User user = userList.get(0);
			return user;
		}
	}

	/**
	 * 重置用户密码
	 * **/
	public int userPassReset(User user) {
		// 验证就旧的密码与当前密码是否一致
		try {
			user.setPassword(MD5Utils.getEncryptedPwd(password));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		user.setModifyDate(new Date(System.currentTimeMillis()));
		return userMgmtMapper.updateUser(user);
	}

}
