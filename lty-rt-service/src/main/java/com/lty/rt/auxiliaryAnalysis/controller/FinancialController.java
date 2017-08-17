package com.lty.rt.auxiliaryAnalysis.controller;



import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.auxiliaryAnalysis.entity.Financial;
import com.lty.rt.auxiliaryAnalysis.service.FinancialService;
import com.lty.rt.comm.bean.RTResponse;

/**
 * 公交行业人车成本
 * 
 * @author fc
 *
 */
@RequestMapping("/financial")
@Controller
public class FinancialController {

	@Autowired
	private FinancialService financialService;
	
	private static final Logger logger  =  LoggerFactory.getLogger(FinancialController.class);
	
	/**
	 * 所有类型
	 */
	@RequestMapping("/getFinancials")
	@ResponseBody
	public RTResponse getFinancial(Financial param) {
		RTResponse res = new RTResponse();
		logger.info(FinancialController.class + " - getFinancials");
		res.setData(financialService.getFinancials(param));
		return res;
	}
	
	
	/**
	 * 增加
	 */
	@RequestMapping("/addFinancial")
	@ResponseBody
	public RTResponse addFinancial(Financial financial) {
		RTResponse res = new RTResponse();
		financialService.setFinancial(financial);
		logger.info(FinancialController.class + " - addFinancial");
		return res;
	}
	
	
	/**
	 * 删除
	 */
	@RequestMapping("/delFinancial")
	@ResponseBody
	public RTResponse delFinancial(Financial financial) {
		RTResponse res = new RTResponse();
		financialService.delFinancial(financial.getFinancial_id());
		logger.info(FinancialController.class + " - delFinancialType");
		return res;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/updFinancial")
	@ResponseBody
	public RTResponse updFinancial(Financial financial) {
		RTResponse res = new RTResponse();
		financialService.updFinancial(financial);
		logger.info(FinancialController.class + " - updFinancial");
		return res;
	}
	
	
	/**
	 * 渲染图表数据，包含 折线图和 饼图
	 */
	@RequestMapping("/getCharts")
	@ResponseBody
	public RTResponse getCharts(Financial financial) {
		RTResponse res = new RTResponse();
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("line",financialService.queryLineFinancialsByCondition(financial));
		data.put("pie",financialService.queryPieFinancialsByCondition(financial));
		res.setData(data);
		logger.info(FinancialController.class + " - updFinancial");
		return res;
	}
	
	
	
}
