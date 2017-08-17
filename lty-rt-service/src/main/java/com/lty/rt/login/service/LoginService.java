package com.lty.rt.login.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lty.rt.sysMgmt.entity.User;
import com.lty.rt.sysMgmt.service.UserMgmtService;
import com.lty.rt.sysMgmt.utils.MD5Utils;

@Service
public class LoginService {
	
	@Autowired
	private UserMgmtService userMgmtService;
	
	public User login(Map<String, String> map){
		if(map == null){
			throw new RuntimeException("paramer is wrong!");
		}
		
		String username = map.get("username");
		String password = map.get("password");
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
			throw new RuntimeException("paramer is null!");
		}
		
		User user = userMgmtService.queryUserByName(username);
		if(user == null){
			throw new RuntimeException("query user is null!");
		}
		
		try {
				//校验成功则返回用户
				if(MD5Utils.validPassword(password, user.getPassword())){
					return user;
				}else{
					return null; 
				}
			
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new RuntimeException("validate password exception!");
		}
	}
}
