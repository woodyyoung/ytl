package com.lty.rt.sysMgmt.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;
import com.lty.rt.sysMgmt.entity.User;
import com.lty.rt.sysMgmt.service.UserMgmtService;
import com.lty.rt.sysMgmt.utils.MD5Utils;

/**
 * 用户管理 控制层
 * 
 * @author yyw
 *
 */
@Controller
@RequestMapping("/userMgmt")
public class UserMgmtController {

	private static final Logger logger = LoggerFactory.getLogger(UserMgmtController.class);

	@Autowired
	private UserMgmtService userMgmtService;

	/**
	 * 查询所有用户
	 * 
	 * @return 用户集合
	 */
	@RequestMapping("/queryAllUser")
	@ResponseBody
	public Response<List<User>> queryAllUser() {
		Response<List<User>> result = new Response<List<User>>();
		try {
			List<User> userList = userMgmtService.queryAllUser();
			result.setCode(ReturnCode.SUCCESS.getCode());
			result.setMsg(ReturnCode.SUCCESS.getMsg());
			result.setData(userList);
		} catch (Exception e) {
			result.setCode(ReturnCode.ERROR_01.getCode());
			result.setMsg(ReturnCode.ERROR_01.getMsg());
			logger.error("query user error! ", e);
		}
		return result;
	}

	@RequestMapping("/queryUserById")
	@ResponseBody
	public Response<User> queryUserById(@RequestBody Map<String, String> params) {
		Response<User> result = new Response<User>();
		try {
			String id = params.get("id");
			User user = userMgmtService.queryUserById(id);
			result.setCode(ReturnCode.SUCCESS.getCode());
			result.setMsg(ReturnCode.SUCCESS.getMsg());
			result.setData(user);
		} catch (Exception e) {
			result.setCode(ReturnCode.ERROR_01.getCode());
			result.setMsg(ReturnCode.ERROR_01.getMsg());
			logger.error("query user error! ", e);
		}
		return result;
	}

	@RequestMapping("/saveUser")
	@ResponseBody
	public Response<Integer> saveUser(@RequestBody User user) {
		Response<Integer> result = new Response<Integer>();
		try {
			int count = userMgmtService.saveUser(user);
			if (count > 0) {
				result.setCode(ReturnCode.SUCCESS.getCode());
				result.setMsg(ReturnCode.SUCCESS.getMsg());
				result.setData(0);
			} else {
				result.setCode(ReturnCode.ERROR_05.getCode());
				result.setMsg(ReturnCode.ERROR_05.getMsg());
				result.setData(0);
				logger.error("save user failed!");
			}
		} catch (Exception e) {
			result.setCode(ReturnCode.ERROR_01.getCode());
			result.setMsg(ReturnCode.ERROR_01.getMsg());
			logger.error("save user error! ", e);
		}
		return result;
	}

	@RequestMapping("/delUser")
	@ResponseBody
	public Response<Integer> delUser(@RequestBody Map<String, String> params) {
		Response<Integer> result = new Response<Integer>();
		try {
			String id = params.get("id");
			userMgmtService.delUser(id);
			result.setCode(ReturnCode.SUCCESS.getCode());
			result.setMsg(ReturnCode.SUCCESS.getMsg());
			result.setData(0);
		} catch (Exception e) {
			result.setCode(ReturnCode.ERROR_01.getCode());
			result.setMsg(ReturnCode.ERROR_01.getMsg());
			logger.error("delete user error! ", e);
		}
		return result;
	}
	
	@RequestMapping("/userPassReset")
	@ResponseBody
	public Response<Integer> userPassReset(@RequestBody Map<String, String> params) {
		Response<Integer> result = new Response<Integer>();
		try {
			String id = params.get("id");
			User user = userMgmtService.queryUserById(id);
			int count = userMgmtService.userPassReset(user);
			
			if (count > 0 || count == -2147482646) {
				result.setCode(ReturnCode.SUCCESS.getCode());
				result.setMsg(ReturnCode.SUCCESS.getMsg());
				result.setData(0);
			} else {
				result.setCode(ReturnCode.ERROR_05.getCode());
				result.setMsg(ReturnCode.ERROR_05.getMsg());
				result.setData(1);
				logger.error("delete user failed!");
			}
		} catch (Exception e) {
			result.setCode(ReturnCode.ERROR_01.getCode());
			result.setMsg(ReturnCode.ERROR_01.getMsg());
			logger.error("delete user error! ", e);
		}
		return result;
	}
	
	/**
	 * 修改登录用户的密码
	 * @param params 参数列表
	 * @return 返回操作代码
	 * 
	 * count为 -1的话旧密码不匹配
	 */
	@RequestMapping("/updateUserPwd")
	@ResponseBody
	public Response<Integer> updateUserPwd(@RequestBody Map<String, String> params,HttpServletRequest request){
		Response<Integer> result = new Response<Integer>();
		try {
			User user = (User) request.getSession().getAttribute("loginUser");
			user.setPassword(MD5Utils.getEncryptedPwd(params.get("newPWD")));
			int count = userMgmtService.updateUser(user,params.get("oldPWD"));
			/*
			 * 配置文件设置了 BATCH,所有增，删，改，操作返回值为-2147482646
			 *count == -2147482646 为
			 */
			if (count > 0 || count == -2147482646) {
				result.setCode(ReturnCode.SUCCESS.getCode());
				result.setMsg(ReturnCode.SUCCESS.getMsg());
				result.setData(0);
			} else {
				result.setCode(count);
				result.setMsg(ReturnCode.ERROR_05.getMsg());
				result.setData(1);
				logger.error("update user password failed!");
			}
		} catch (Exception e) {
			result.setCode(ReturnCode.ERROR_01.getCode());
			result.setMsg(ReturnCode.ERROR_01.getMsg());
			logger.error("update user password error! ", e);
		}
		
		return result;
	}
	
	/**
	 * 修改登录用户的密码
	 * @param params 参数列表
	 * @return 返回操作代码
	 * 
	 */
	@RequestMapping("/updatePwd")
	@ResponseBody
	public Response<Integer> updatePwd(@RequestBody Map<String, String> params,HttpServletRequest request){
		Response<Integer> result = new Response<Integer>();
		try {
			User user = new User();
			user.setId(params.get("userId"));
			user.setPassword(params.get("pwd"));
			userMgmtService.saveUser(user);
			result.setCode(1);
			result.setData(0);
		} catch (Exception e) {
			result.setCode(ReturnCode.ERROR_01.getCode());
			result.setMsg(ReturnCode.ERROR_01.getMsg());
			logger.error("update user password error! ", e);
		}
		return result;
	}
	
	
	
	/**
	 * 获取Session里的用户信息
	 * @return
	 */
	@RequestMapping("/getSessionUserInfo")
	@ResponseBody
	public String getSessionUserInfo(HttpServletRequest request){
		try {
			User user = (User) request.getSession().getAttribute("loginUser");
			if (user!=null) {
				return user.getId();
			} 
		} catch (Exception e) {
			logger.error("update user password error! ", e);
		}
		return null;
	}
}
