package com.lty.rt.auxiliaryAnalysis.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.auxiliaryAnalysis.entity.PVCost;
import com.lty.rt.auxiliaryAnalysis.service.PVCostService;
import com.lty.rt.comm.bean.RTResponse;

/**
 * 公交行业人车成本
 * 
 * @author fc
 *
 */
@RequestMapping("/pvcost")
@Controller
public class PVCostController {

	@Autowired
	private PVCostService pVCostService;
	
	private static final Logger logger  =  LoggerFactory.getLogger(PVCostController.class);
	
	/**
	 * 所有类型
	 */
	@RequestMapping("/getPVCosts")
	@ResponseBody
	public RTResponse getPVCosts(PVCost param) {
		RTResponse res = new RTResponse();
		logger.info(PVCostController.class + " - getPVCosts");
		res.setData(pVCostService.getPVCosts(param));
		return res;
	}
	
	
	/**
	 * 所有类型
	 */
	@RequestMapping("/queryPVCostsByCondition")
	@ResponseBody
	public RTResponse queryPVCostsByCondition(PVCost param) {
		RTResponse res = new RTResponse();
		logger.info(PVCostController.class + " - getPVCosts");
		res.setData(pVCostService.queryPVCostsByCondition(param));
		return res;
	}
	
	
	/**
	 * 增加
	 */
	@RequestMapping("/addPVCost")
	@ResponseBody
	public RTResponse addPVCost(PVCost pVCost) {
		RTResponse res = new RTResponse();
		pVCostService.setPVCost(pVCost);
		return res;
	}
	
	
	/**
	 * 删除
	 */
	@RequestMapping("/delPVCost")
	@ResponseBody
	public RTResponse delPVCostType(PVCost pVCost) {
		RTResponse res = new RTResponse();
		pVCostService.delPVCost(pVCost.getPvcost_id());
		return res;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/updPVCost")
	@ResponseBody
	public RTResponse updPVCost(PVCost pVCost) {
		RTResponse res = new RTResponse();
		pVCostService.updPVCost(pVCost);
		return res;
	}
	
	/**
	 * 查询所有基础数据
	 */
	@RequestMapping("/getDepartments")
	@ResponseBody
	public RTResponse getDepartments() {
		RTResponse res = new RTResponse();
		res.setData(pVCostService.getDepartments());
		return res;
	}
}
