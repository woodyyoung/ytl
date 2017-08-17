package com.lty.rt.auxiliaryAnalysis.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.auxiliaryAnalysis.entity.BusRevenue;
import com.lty.rt.auxiliaryAnalysis.service.BusRevenueService;
import com.lty.rt.basicData.bean.PjmkArea;
import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;
import com.lty.rt.districtManagement.controller.DistrictManageController;
import com.lty.rt.sysMgmt.entity.Role;
import com.lty.rt.sysMgmt.entity.User;

/**
 * 营收录入
 * 
 * @author yyw
 *
 */
@RequestMapping("/busrevenue")
@Controller
public class BusRevenueController {
	@Autowired
	private BusRevenueService busRevenumService;
	
	private static final Logger logger  =  LoggerFactory.getLogger(DistrictManageController.class);
	
	/**
	 * 查询所有
	 */
	@RequestMapping("/busrevenumlist")
	@ResponseBody
	public Response<List<BusRevenue>> getBusRevenumlist(@RequestBody Map<String, Object> map) {
		Response<List<BusRevenue>> resp = new Response<List<BusRevenue>>();
		try{
			List<BusRevenue> list = busRevenumService.listAll(map);
			resp.setData(list);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("districtManagement.getDistrictList() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}
	
	@RequestMapping("/delById")
	@ResponseBody
	public Response<Integer> delById(@RequestBody Map<String, String> params){
		Response<Integer>  resp = new Response<Integer> ();
		String id = params.get("id");
		int count = busRevenumService.deleteByAreaCode(id);
		try{
			if(StringUtils.isBlank(id)){
				resp.setCode(ReturnCode.ERROR_03.getCode());
				resp.setMsg(ReturnCode.ERROR_03.getMsg());
			}else{
				resp.setData(count);
				resp.setCode(ReturnCode.SUCCESS.getCode());
				resp.setMsg(ReturnCode.SUCCESS.getMsg());
			}
		} catch (ApplicationException e) {
			logger.error("districtManagement.delById() error{}",e);
			resp.setCode(e.getCode());
			resp.setMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("districtManagement.delById() error{}",e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;

	}
	
	/**
	 * 保存营收信息
	 * @param out
	 * @param role
	 * @throws ParseException 
	 */
	@RequestMapping("/save")
	@ResponseBody
	public RTResponse save(BusRevenue bus,HttpServletRequest request) throws ParseException{
		if(bus.getId()!=null && bus.getRevenue() > 0){
			bus.convertToYear();
			busRevenumService.insertPjmkArea(bus);
		}else{
			busRevenumService.insertPjmkArea(bus);
		}
		RTResponse resp = new RTResponse();
		return resp;
	}
	
	
	@RequestMapping("/getIndustryData")
	@ResponseBody
	public Map<String, Object> getIndustryData(@RequestBody Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			retMap = busRevenumService.getIndustryData(map);
		} catch (Exception e) {
			logger.error("industryCost.getIndustryData() error{}", e);
		}
		return retMap;
	}
	
	@RequestMapping(value = "/getIndustryPieData")
	@ResponseBody
	public Map<String, Object> getIndustryPieData(@RequestBody Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			retMap = busRevenumService.getIndustryPieData(map);
		} catch (Exception e) {
			logger.error("industryCost.getIndustryData() error{}", e);
		}
		return retMap;
	}
}
