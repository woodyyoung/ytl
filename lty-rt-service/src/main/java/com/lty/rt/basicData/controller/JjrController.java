package com.lty.rt.basicData.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.basicData.service.JjrService;
import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.comm.util.DateUtil;
import com.lty.rt.sysMgmt.entity.User;

/**
 * 
 * @author tanhuangyong 2017-04-12
 *
 */
@RequestMapping("/jjr")
@Controller
public class JjrController {
	
	static private Log log = LogFactory.getLog(JjrController.class);
	
	@Autowired(required=true)
	private JjrService jjrService;
	
	/**
	 * 根据月份返回该月对应的所有种类的日期
	 * @param mouth 参数格式"201704"
	 * @return
	 */
	@RequestMapping(value="/initMouth", method=RequestMethod.GET)
	@ResponseBody
	public RTResponse initByMouth(@RequestParam("mouth") String mouth){
		RTResponse response =  null;
		try{
			
			Map<String, List<String>> result = jjrService.selectBymonth(mouth);

			response =  new RTResponse();
			response.setResCode(200);
			response.setData(result);
			
		}catch(Throwable ex){
			
			log.error(ex);
			
			response =  new RTResponse();
			response.setResCode(300);
			response.setMsg(ex.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 支持一次性传递多种日期类型，达到批量设置的目的
	 * @param states  由前端传入,参数自动注入
	 * @param httpSession 
	 * @return
	 */
	@RequestMapping(value="/change", method=RequestMethod.POST)
	@ResponseBody
	public RTResponse  changeState(@RequestBody Map<String, List<String>> states, HttpSession httpSession){
	
		RTResponse response = null;
		try{
			
			if(states.keySet().size()==0){
				throw new IllegalArgumentException("参数为空");
			}
			Object ousername = states.get("username");
			String username = null;
			if(ousername!=null){
				username = ousername.toString();
				states.remove("username");
			}else{
				User user = (User) httpSession.getAttribute("loginUser");
				if(user == null){
					throw new IllegalArgumentException("获取用户信息失败");
				}
				username = user.getUserName();
			}
			
			Map<String, List<Date>> states2 = new HashMap<String, List<Date>>();
			for(String state : states.keySet()){
				List<String> str_dates = states.get(state);
				if(str_dates.size()==0){
					continue;
				}
				List<Date> dates = new ArrayList<Date>(str_dates.size());
				for(String str_date : str_dates){
					try{
						Date date = DateUtil.convertStringToDate(DateUtil.DATE, str_date);
						dates.add(date);
					}catch(ParseException ex){
						throw new IllegalArgumentException("时间参数格式不对", ex);
					}
				}
				states2.put(state, dates);
			}
			
			jjrService.changeState(states2, username);
			
			response =  new RTResponse();
			response.setResCode(200);
			
		}catch(Throwable ex){
			log.error(ex);
			
			response =  new RTResponse();
			response.setResCode(300);
			response.setMsg(ex.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 返回查询区间段内的所有节假日数据，不返回工作日和非工作日数据
	 * @param params 目前是前端分页，用post来传递参数，考虑后面升级为后端分页，保证前端接口调用逻辑不用变
	 * @return
	 */
	@RequestMapping(value="/allJjr", method=RequestMethod.POST)
	@ResponseBody
	public RTResponse selectAll(@RequestBody Map<String, String> params){
		RTResponse response =  null;
		try{
			String str_begin = params.get("begin");
			Date begin = null ;
			if(StringUtils.hasText(str_begin)){
				begin =  DateUtil.convertStringToDate(DateUtil.DATE, str_begin);
			}
			String str_end = params.get("end");
			Date end = null;
			if(StringUtils.hasText(str_end)){
				end = DateUtil.convertStringToDate(DateUtil.DATE, str_end);
			}
			List<Map<String, String>> rows = jjrService.selectAll(begin,  end);
			
			response =  new RTResponse();
			response.setResCode(200);
			response.setData(rows);
			
		}catch(ParseException ex){
			log.error(ex);
			
			response =  new RTResponse();
			response.setResCode(300);
			response.setMsg("时间格式不正确");
		}
		catch(Throwable ex){
			log.error(ex);
			
			response =  new RTResponse();
			response.setResCode(300);
			response.setMsg(ex.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value="/flush", method=RequestMethod.GET)
	@ResponseBody
	public RTResponse flush(){
		RTResponse response =  null;
		try{
			
			jjrService.flush();
			response =  new RTResponse();
			response.setResCode(200);
		
		}catch(Throwable ex){
			log.error(ex);
			
			response =  new RTResponse();
			response.setResCode(300);
			response.setMsg(ex.getMessage());
		}
		return response;
	}
	
	

}
