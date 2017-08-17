package com.lty.rt.login.controller;

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
import com.lty.rt.login.service.LoginService;
import com.lty.rt.sysMgmt.entity.User;

/**
 * 登录
 * 
 * @author yyw
 */
@RequestMapping("/userLogin")
@Controller
public class LoginController {

	private static final Logger logger  =  LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/login")
	@ResponseBody
	public Response<User> login(@RequestBody Map<String, String> params,HttpServletRequest request){
		Response<User> result = new Response<User>();
		try {
			result.setCode(ReturnCode.SUCCESS.getCode());
			result.setMsg(ReturnCode.SUCCESS.getMsg());
			
			//获取登录成功的用户保存到会话
			User user =  loginService.login(params);
			request.getSession().setAttribute("loginUser", user);
			
			result.setData(user);
		} catch (Exception e) {
			result.setCode(ReturnCode.ERROR_01.getCode());
			result.setMsg(ReturnCode.ERROR_01.getMsg());
			logger.error("login failed! ", e);
		}
		return result;
	}
}
