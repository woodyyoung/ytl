package com.lty.rt.passengerFlows.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.comm.bean.RTResponse;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.util.UUIDUtils;
import com.lty.rt.passengerFlows.bean.IndustryCost;
import com.lty.rt.passengerFlows.service.IndustryCostService;

@RequestMapping("/industryCost")
@Controller
public class IndustryCostController {

	private static final Logger logger = LoggerFactory.getLogger(IndustryCostController.class);

	@Autowired
	private IndustryCostService industryCostService;

	@RequestMapping(value = "/list")
	@ResponseBody
	public List<Map<String, Object>> list(@RequestBody Map<String, Object> map) {
		return industryCostService.findListByMap(map);
	}

	@RequestMapping(value = "/getIndustryData")
	@ResponseBody
	public Map<String, Object> getIndustryData(@RequestBody Map<String, Object> map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			retMap = industryCostService.getIndustryData(map);
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
			retMap = industryCostService.getIndustryPieData(map);
		} catch (Exception e) {
			logger.error("industryCost.getIndustryData() error{}", e);
		}
		return retMap;
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public IndustryCost edit(String industryCostId) {
		if (StringUtils.isNotBlank(industryCostId)) {
			return industryCostService.selectByPrimaryKey(industryCostId);
		}
		return null;
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	@Transactional
	public RTResponse save(IndustryCost industryCost) {
		RTResponse res = new RTResponse();
		try {
			IndustryCost isExists = industryCostService.selectByPrimaryKey(industryCost.getId());
			if (isExists != null) {
				Map<String, Object> conditionMap = new HashMap<String, Object>();
				conditionMap.put("occurtime", industryCost.getOccurtime());
				conditionMap.put("costType", industryCost.getCostTypeNo());
				IndustryCost yearIsExists = industryCostService.selectByYearAndCostType(conditionMap);
				if (yearIsExists == null) {
					industryCostService.updateByPrimaryKey(industryCost);
					res.setResCode(ReturnCode.SUCCESS.getCode());
				} else {
					res.setMsg("存在重复业务数据");
					res.setResCode(ReturnCode.ERROR_01.getCode());
				}
			} else {
				Map<String, Object> conditionMap = new HashMap<String, Object>();
				conditionMap.put("occurtime", industryCost.getOccurtime());
				conditionMap.put("costType", industryCost.getCostTypeNo());
				IndustryCost yearIsExists = industryCostService.selectByYearAndCostType(conditionMap);
				if (yearIsExists == null) {
					industryCost.setId(UUIDUtils.getUUID().toString());
					industryCostService.insert(industryCost);
				} else {
					res.setMsg("存在重复业务数据");
					res.setResCode(ReturnCode.ERROR_01.getCode());
				}
			}
		} catch (Exception e) {
			res.setResCode(ReturnCode.ERROR_01.getCode());
			res.setMsg(ReturnCode.ERROR_01.getMsg());
			logger.error("industryCost.save() error{}", e);
		}
		return res;
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public void delete(String industryCostId) {
		try {
			if (StringUtils.isNotBlank(industryCostId)) {
				// industryCostService.deleteByPrimaryKey(industryCostId);
				industryCostService.deleteByPrimaryKey(industryCostId.split(","));
			}
		} catch (Exception e) {
			logger.error("industryCost.delete() error{}", e);
		}
	}

}
