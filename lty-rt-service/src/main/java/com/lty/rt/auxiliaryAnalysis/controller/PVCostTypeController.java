package com.lty.rt.auxiliaryAnalysis.controller;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lty.rt.auxiliaryAnalysis.entity.PVCostType;
import com.lty.rt.auxiliaryAnalysis.service.PVCostTypeService;
import com.lty.rt.comm.bean.RTResponse;

/**
 * 公交行业人车成本类型
 * 
 * @author fc
 *
 */

@RequestMapping("/pvcosttype")
@Controller
public class PVCostTypeController {
	@Autowired
	private PVCostTypeService pVCostTypeService;
	
	private static final Logger logger  =  LoggerFactory.getLogger(PVCostTypeController.class);
	
	/**
	 * 所有类型
	 */
	@RequestMapping("/getPVCostTypes")
	@ResponseBody
	public RTResponse getPVCostTypes() {
		RTResponse res = new RTResponse();
		logger.info(PVCostTypeController.class + " - getPVCostTypes");
		res.setData(pVCostTypeService.getPVCostTypes());
		return res;
	}
	
	
	/**
	 * 增加
	 */
	@RequestMapping("/addPVCostType")
	@ResponseBody
	public RTResponse addPVCostType(PVCostType pVCostType) {
		RTResponse res = new RTResponse();
		pVCostTypeService.setPVCostType(pVCostType);
		return res;
	}
	
	
	/**
	 * 删除
	 */
	@RequestMapping("/delPVCostTypes")
	@ResponseBody
	public RTResponse delPVCostTypes(@RequestParam(value = "pvcosttype_id[]") String[] pvcosttype_id) {
		RTResponse res = new RTResponse();
		//pVCostTypeService.delPVCostTypes(pvcosttype_id);
		return res;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delPVCostType")
	@ResponseBody
	public RTResponse delPVCostType(PVCostType pVCostType) {
		RTResponse res = new RTResponse();
		pVCostTypeService.delPVCostType(pVCostType.getPvcosttype_id());
		return res;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/updPVCostType")
	@ResponseBody
	public RTResponse updPVCostType(PVCostType pVCostType) {
		RTResponse res = new RTResponse();
		pVCostTypeService.updPVCostType(pVCostType);
		return res;
	}
}
