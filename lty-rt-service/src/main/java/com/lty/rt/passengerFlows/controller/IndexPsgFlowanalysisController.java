package com.lty.rt.passengerFlows.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.passengerFlows.service.IndexPsgFlowAnalysisService;

/**
 * 首页数据展示
 * 
 * @author YUJI
 */

@RequestMapping("/index")
@Controller
public class IndexPsgFlowanalysisController {
	private static final Logger logger = LoggerFactory.getLogger(IndexPsgFlowanalysisController.class);

	@Autowired
	private IndexPsgFlowAnalysisService indexPsgFlowAnalysisService;

	@RequestMapping("/getBasicPsgFlowData")
	@ResponseBody
	public RTResponse indexBasciData() {
		RTResponse res = new RTResponse();
		try {
			res.setData(indexPsgFlowAnalysisService.queryMaxPsgFlowData());

		} catch (Exception e) {
			logger.error("index.indexBasciData() error:{}" + e);
		}
		return res;
	}

}
