package com.lty.rt.passengerFlows.controller;

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
import com.lty.rt.comm.util.UUIDUtils;
import com.lty.rt.passengerFlows.bean.IndustryCostType;
import com.lty.rt.passengerFlows.service.IndustryCostTypeService;

@RequestMapping("/industryCostType")
@Controller
public class IndustryCostTypeController {

	private static final Logger logger = LoggerFactory.getLogger(IndustryCostTypeController.class);

	@Autowired
	private IndustryCostTypeService industryCostTypeService;

	@RequestMapping(value = "/list")
	@ResponseBody
	public List<Map<String, Object>> list(@RequestBody Map<String, Object> map) {
		return industryCostTypeService.findListByMap(map);
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public IndustryCostType edit(String industryCostTypeId) {
		if (StringUtils.isNotBlank(industryCostTypeId)) {
			return industryCostTypeService.selectByPrimaryKey(industryCostTypeId);
		}
		return null;
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	@Transactional
	public RTResponse save(IndustryCostType industryCostType) {
		RTResponse rt = new RTResponse();
		try {
			IndustryCostType isExists = industryCostTypeService.selectByPrimaryKey(industryCostType.getId());
			if (isExists != null) {
				industryCostTypeService.updateByPrimaryKey(industryCostType);
			} else {
				industryCostType.setId(UUIDUtils.getUUID().toString());
				industryCostType.setTypeNo(UUIDUtils.getUUID().toString());
				industryCostTypeService.insert(industryCostType);
			}
		} catch (Exception e) {
			logger.error("industryCost.save() error{}", e);
			rt.setResCode(-1);
			rt.setMsg(e.getMessage());
		}
		return rt;
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public RTResponse delete(String industryCostId) {
		RTResponse rt = new RTResponse();
		try {
			if (StringUtils.isNotBlank(industryCostId)) {
				// industryCostService.deleteByPrimaryKey(industryCostId);
				industryCostTypeService.deleteByPrimaryKey(industryCostId.split(","));
			}
		} catch (Exception e) {
			logger.error("industryCost.delete() error{}", e);
			rt.setResCode(-1);
			rt.setMsg(e.getMessage());
		}
		return rt;
	}

}
