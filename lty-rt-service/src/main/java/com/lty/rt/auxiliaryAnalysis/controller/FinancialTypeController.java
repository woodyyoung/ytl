package com.lty.rt.auxiliaryAnalysis.controller;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.auxiliaryAnalysis.entity.FinancialType;
import com.lty.rt.auxiliaryAnalysis.service.FinancialTypeService;
import com.lty.rt.comm.bean.RTResponse;

/**
 * 公交行业人车成本类型
 * 
 * @author fc
 *
 */

@RequestMapping("/financialtype")
@Controller
public class FinancialTypeController {
	@Autowired
	private FinancialTypeService financialTypeService;
	
	private static final Logger logger  =  LoggerFactory.getLogger(FinancialTypeController.class);
	
	/**
	 * 所有类型
	 */
	@RequestMapping("/getFinancialTypes")
	@ResponseBody
	public RTResponse getFinancialTypes(FinancialType financialType) {
		RTResponse res = new RTResponse();
		logger.info(FinancialTypeController.class + " - getFinancialTypes");
		res.setData(financialTypeService.getFinancialTypes(financialType));
		return res;
	}
	
	
	/**
	 * 增加
	 */
	@RequestMapping("/addFinancialType")
	@ResponseBody
	public RTResponse addFinancialType(FinancialType financialType) {
		RTResponse res = new RTResponse();
		financialTypeService.setFinancialType(financialType);
		logger.info(FinancialTypeController.class + " - addFinancialType");
		return res;
	}
	
	
	/**
	 * 批量删除
	 */
	@RequestMapping("/delFinancialTypes")
	@ResponseBody
	public RTResponse delFinancialTypes(@RequestParam(value = "pvcosttype_id[]") String[] pvcosttype_id) {
		RTResponse res = new RTResponse();
		logger.info(FinancialTypeController.class + " - delFinancialTypes");
		return res;
	}
	
	/**
	 * 单个删除
	 */
	@RequestMapping("/delFinancialType")
	@ResponseBody
	public RTResponse delFinancialType(FinancialType financialType) {
		RTResponse res = new RTResponse();
		financialTypeService.delFinancialType(financialType.getFinancialtype_id());
		logger.info(FinancialTypeController.class + " - delFinancialType");
		return res;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/updFinancialType")
	@ResponseBody
	public RTResponse updFinancialType(FinancialType financialType) {
		RTResponse res = new RTResponse();
		financialTypeService.updFinancialType(financialType);
		logger.info(FinancialTypeController.class + " - updFinancialType");
		return res;
	}
}
