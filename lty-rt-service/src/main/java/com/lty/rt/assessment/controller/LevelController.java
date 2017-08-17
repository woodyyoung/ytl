package com.lty.rt.assessment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.assessment.bean.Level;
import com.lty.rt.assessment.service.LevelService;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;

/**
 * 评分等级维护
 * @author qiq
 *
 */

@RequestMapping("/level")
@Controller
public class LevelController {
	
	private static final Logger logger  =  LoggerFactory.getLogger(LevelController.class);

	@Autowired
	private LevelService levelService;
	
	@RequestMapping("/findLevels")
	@ResponseBody
	public Response<List<Level>> findLevels(){
		Response<List<Level>> resp = new Response<List<Level>>();
		try{
			List<Level> list = levelService.findLevels();
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("level.findLevels() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/getLevelById")
	@ResponseBody
	public Response<Level> getLevelById(@RequestBody String dataId){
		Response<Level> resp = new Response<Level>();
		try{
			Level level = levelService.findById(dataId);
			resp.setData(level);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (Exception e) {
			logger.error("level.getLevelById() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/insertLevel")
	@ResponseBody
	public Response<Integer> insertLevel(Level level){
		Response<Integer> resp = new Response<Integer>();
		try{
			Integer count = levelService.saveOrUpdate(level);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (ApplicationException el) {
			logger.error("level.delByIds() error{}",el);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(el.getMessage());
		}catch (Exception e) {
			logger.error("level.insertLevel() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
		
	}
	
	
	@RequestMapping("/delByIds")
	@ResponseBody
	public Response<Integer> delByIds(@RequestBody String dataIds){
		Response<Integer> resp = new Response<Integer>();
		try{
			Integer count = levelService.delByIds(dataIds);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		}catch (ApplicationException el) {
			logger.error("level.delByIds() error{}",el);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(el.getMessage());
		}catch (Exception e) {
			logger.error("level.delByIds() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	

}
