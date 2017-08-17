package com.lty.rt.districtManagement.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;
import com.lty.rt.districtManagement.service.StretchPlatformService;
import com.lty.rt.districtManagement.vo.AreaPlatResultVo;

/**
 * 路段站台管理
 * @author qiq
 *
 */
@RequestMapping("/stretchPlatformManagement")
@Controller
public class StretchPlatformManageController {
	
	private static final Logger logger  =  LoggerFactory.getLogger(AreaMapController.class);
	
	@Autowired
	private StretchPlatformService stretchPlatformService;
	
	@RequestMapping("/selectStretchPlat")
	@ResponseBody
	public Response<List<Map<String,Object>>> selectStretchPlat(@RequestBody String lineId) {
		Response<List<Map<String,Object>>> resp = new Response<List<Map<String,Object>>>();
		try{
			List<Map<String,Object>> list =  stretchPlatformService.selectStretchPlatform(lineId);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("stretchPlatformManagement.selectStretchPlat() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	/**
	 * 查找所有站台信息，已经关联路段的站台不显示
	 * @return
	 */
	@RequestMapping("/getPlatFormList")
	@ResponseBody
	public Response<List<Map<String,Object>>> getPlatFormList(){
		Response<List<Map<String,Object>>> resp = new Response<List<Map<String,Object>>>();
		try{
			List<Map<String,Object>> list = stretchPlatformService.findAllAvaliblePlat();
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("stretchPlatformManagement.getPlatFormList() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	/**
	 * 查询路段未关联的所有站台
	 * @return
	 */
	@RequestMapping("/findNotRefrencePlatfom")
	@ResponseBody
	public Response<List<Map<String,Object>>> findNotRefrencePlatfom(@RequestBody String lineId){
		Response<List<Map<String,Object>>> resp = new Response<List<Map<String,Object>>>();
		try{
			List<Map<String,Object>> list = stretchPlatformService.findNotRefrencePlatfom(lineId);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("stretchPlatformManagement.findNotRefrencePlatfom() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	
	/**
	 * 路段添加站台
	 * @return
	 */
	@RequestMapping("/insertFromPlat")
	@ResponseBody
	public Response<Integer> insertFromPlat(@RequestBody Map<String, Object> map){
		Response<Integer> resp = new Response<Integer>();
		try{
			String stretchid = (String)map.get("stretchid");
			String platFormId = (String)map.get("platFormId");
			if(StringUtils.isBlank(stretchid) || StringUtils.isBlank(platFormId)){
				resp.setCode(ReturnCode.ERROR_02.getCode());
				resp.setMsg(ReturnCode.ERROR_02.getMsg());
			} else {
				int count = stretchPlatformService.insertIntoPlat(stretchid.split(",")[0], stretchid.split(",")[1], platFormId.split(","));
				resp.setData(count);
				resp.setCode(ReturnCode.SUCCESS.getCode());
				resp.setMsg(ReturnCode.SUCCESS.getMsg());
			}
		} catch (Exception e) {
			logger.error("stretchPlatformManagement.insertAreaMap() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/delByIds")
	@ResponseBody
	public Response<Integer> delByIds(@RequestBody Map<String, Object> map){
		Response<Integer> resp = new Response<Integer>();
		try{
			String stretchId = (String)map.get("ldid");
			String platFormId = (String)map.get("platFormId");
			if(StringUtils.isBlank(stretchId) || StringUtils.isBlank(platFormId)){
				resp.setCode(ReturnCode.ERROR_02.getCode());
				resp.setMsg(ReturnCode.ERROR_02.getMsg());
			}else {
				int count = stretchPlatformService.delByIds(stretchId, platFormId.split(","));
				resp.setData(count);
				resp.setCode(ReturnCode.SUCCESS.getCode());
				resp.setMsg(ReturnCode.SUCCESS.getMsg());
			}
			
		} catch (Exception e) {
			logger.error("stretchPlatformManagement.insertAreaMap() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
}
