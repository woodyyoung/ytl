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

import com.lty.rt.basicData.bean.PlatForm;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;
import com.lty.rt.districtManagement.service.AreaPlatformService;
import com.lty.rt.districtManagement.vo.AreaPlatResultVo;


/**
 * 区域站台管理
 * @author qiq
 *
 */
@RequestMapping("/areaStationManagement")
@Controller
public class AreaStationManageController{
	
	private static final Logger logger  =  LoggerFactory.getLogger(AreaStationManageController.class);
	
	@Autowired
	private AreaPlatformService areaPlatformService;
	
	/**
	 * 通过区域得到站台信息
	 * @param areaId
	 * @return
	 */
	@RequestMapping("/selectAreaPlat")
	@ResponseBody
	public Response<List<AreaPlatResultVo>> selectAreaPlat(@RequestBody String areaId) {
		Response<List<AreaPlatResultVo>> resp = new Response<List<AreaPlatResultVo>>();
		try {
			List<AreaPlatResultVo> list = areaPlatformService.selectAreaPlat(areaId);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (ApplicationException ex){
			logger.error("AreaStationManageController.selectAreaPlat() error{}",ex);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		} catch (Exception e) {
			logger.error("AreaStationManageController.selectAreaPlat() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	/**
	 * 通过区域得到站台信息
	 * @param areaId
	 * @return
	 */
	@RequestMapping("/queryAreaPlatform")
	@ResponseBody
	public Response<List<PlatForm>> queryAreaPlatform(@RequestBody String areaId) {
		Response<List<PlatForm>> resp = new Response<List<PlatForm>>();
		try {
			List<PlatForm> list = areaPlatformService.queryAreaPlatform(areaId);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (ApplicationException ex){
			logger.error("AreaStationManageController.queryAreaPlatform() error{}",ex);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		} catch (Exception e) {
			logger.error("AreaStationManageController.queryAreaPlatform() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	/**
	 * 查找所有站台信息，已经关联区域的站台不显示
	 * @return
	 */
	@RequestMapping("/getPlatFormList")
	@ResponseBody
	public Response<List<AreaPlatResultVo>> getPlatFormList(){
		Response<List<AreaPlatResultVo>> resp = new Response<List<AreaPlatResultVo>>();
		try{
			List<AreaPlatResultVo> list = areaPlatformService.findAllAvaliblePlat();
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (ApplicationException ex){
			logger.error("AreaStationManageController.getPlatFormList() error{}",ex);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		} catch (Exception e) {
			logger.error("AreaStationManageController.getPlatFormList() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/queryAvailablePlatform")
	@ResponseBody
	public Response<List<PlatForm>> queryAvailablePlatform(){
		Response<List<PlatForm>> resp = new Response<List<PlatForm>>();
		try{
			List<PlatForm> list = areaPlatformService.queryAvailablePlatform();
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (ApplicationException ex){
			logger.error("AreaStationManageController.getPlatFormList() error{}",ex);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		} catch (Exception e) {
			logger.error("AreaStationManageController.getPlatFormList() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	/**
	 * 区域添加站台
	 * @return
	 */
	@RequestMapping("/insertFromPlat")
	@ResponseBody
	public Response<Integer> insertFromPlat(@RequestBody Map<String, Object> map){
		Response<Integer> resp = new Response<Integer>();
		try{
			String areaId = (String)map.get("areaId");
			String platFormId = (String)map.get("platFormId");
			if(StringUtils.isBlank(areaId) || StringUtils.isBlank(platFormId)){
				resp.setCode(ReturnCode.ERROR_03.getCode());
				resp.setMsg(ReturnCode.ERROR_03.getMsg());
			}else {
				int count = areaPlatformService.insertIntoPlat(areaId.split(",")[0], areaId.split(",")[1], platFormId.split(","));
				resp.setData(count);
				resp.setCode(ReturnCode.SUCCESS.getCode());
				resp.setMsg(ReturnCode.SUCCESS.getMsg());
			}
		} catch (ApplicationException ex){
			logger.error("AreaStationManageController.insertFromPlat() error{}",ex);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		} catch (Exception e) {
			logger.error("AreaStationManageController.insertFromPlat() error{}",e);
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
			String areaId = (String)map.get("quid");
			String platFormId = (String)map.get("platFormId");
			if(StringUtils.isBlank(areaId) || StringUtils.isBlank(platFormId)){
				resp.setCode(ReturnCode.ERROR_03.getCode());
				resp.setMsg(ReturnCode.ERROR_03.getMsg());
			}else {
				int count = areaPlatformService.delByIds(areaId, platFormId.split(","));
				resp.setData(count);
				resp.setCode(ReturnCode.SUCCESS.getCode());
				resp.setMsg(ReturnCode.SUCCESS.getMsg());
			}
		} catch (ApplicationException ex){
			logger.error("AreaStationManageController.insertFromPlat() delByIds{}",ex);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		} catch (Exception e) {
			logger.error("AreaStationManageController.insertFromPlat() delByIds{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		
		return resp;
	}
	
}
