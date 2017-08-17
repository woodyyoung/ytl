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

import com.alibaba.fastjson.JSONArray;
import com.lty.rt.basicData.service.PjmkAreaService;
import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.basicData.treeUtil.TreeNodeUtils;
import com.lty.rt.basicData.treeUtil.TreeViewEntity;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.response.Response;
import com.lty.rt.comm.util.UUIDUtils;
import com.lty.rt.districtManagement.controller.DistrictManageController;
import com.lty.rt.passengerFlows.bean.PassengerFlowLevel;
import com.lty.rt.passengerFlows.bean.PsgLevelPlan;
import com.lty.rt.passengerFlows.service.PassengerFlowLevelService;

@Controller
@RequestMapping(value = "/passengerFlowLevel")
public class PassengerFlowLevelController {
	private static final Logger logger = LoggerFactory.getLogger(DistrictManageController.class);
	@Autowired
	public PassengerFlowLevelService passengerFlowLevelService;
	@Autowired
	public PjmkAreaService pjmkAreaService;

	@RequestMapping(value = "/list")
	@ResponseBody
	public List<Map<String, Object>> list(@RequestBody Map<String, Object> map) {
		String datatype = map.get("dataType") == null ? "" : map.get("dataType").toString();
		if (StringUtils.isNotBlank(datatype)) {
			return passengerFlowLevelService.findListByMap(map);
		}
		return null;
	}

	@RequestMapping(value = "/planAndLevelList")
	@ResponseBody
	public List<PsgLevelPlan> planAndLevelList(@RequestBody Map<String, Object> map) {
		String datatype = map.get("dataType") == null ? "" : map.get("dataType").toString();
		if (StringUtils.isNotBlank(datatype)) {
			return passengerFlowLevelService.getpsgPlanAndLevelByMap(map);
		}
		return null;
	}

	@RequestMapping(value = "/treeList")
	@ResponseBody
	public Response<JSONArray> treeList(@RequestBody Map<String, Object> map) {
		Response<JSONArray> resp = new Response<JSONArray>();
		try {
			List<TreeNode> nodeList = passengerFlowLevelService.findTreeListByMap(map);
			List<TreeViewEntity> treeList = TreeNodeUtils.generateLevelTreeViewEntity(nodeList);
			resp.setData((JSONArray) JSONArray.toJSON(treeList));
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("districtManagement.getDistrictTree() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

	@RequestMapping(value = "/edit")
	@ResponseBody
	public PassengerFlowLevel edit(String passengerFlowLevelId) {
		if (StringUtils.isNotBlank(passengerFlowLevelId)) {
			return passengerFlowLevelService.selectByPrimaryKey(passengerFlowLevelId);
		}
		return null;
	}

	@RequestMapping(value = "/save")
	@ResponseBody
	@Transactional
	public String save(PassengerFlowLevel passengerFlowLevel) {
		String msg = "success";
		try {
			PassengerFlowLevel isExists = passengerFlowLevelService.selectByPrimaryKey(passengerFlowLevel.getId());
			if (isExists != null) {
				passengerFlowLevelService.updateByPrimaryKey(passengerFlowLevel);
			} else {
				passengerFlowLevel.setId(UUIDUtils.getUUID().toString());
				passengerFlowLevelService.insert(passengerFlowLevel);
			}
		} catch (Exception e) {
			msg = "failed";
		}
		return msg;
	}

	@RequestMapping(value = "/planClose")
	@ResponseBody
	@Transactional
	public Response<Integer> planClose(@RequestBody Map<String, Object> map) {
		Response<Integer> resp = new Response<Integer>();
		try {
			passengerFlowLevelService.close(map);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("passengerFlowLevel.planClose() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

	@RequestMapping(value = "/planInsert")
	@ResponseBody
	@Transactional
	public Response<Integer> planInsert(PsgLevelPlan psgLevelPlan) {
		Response<Integer> resp = new Response<Integer>();
		try {
			psgLevelPlan = passengerFlowLevelService.save(psgLevelPlan);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("passengerFlowLevel.planInsert() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

	@RequestMapping(value = "/batchInsert")
	@ResponseBody
	@Transactional
	public Response<Integer> batchInsert(@RequestBody Map<String, Object> map) {
		Response<Integer> resp = new Response<Integer>();
		try {
			int count = passengerFlowLevelService.batchSave(map);
			resp.setData(count);
			resp.setCode(ReturnCode.SUCCESS.getCode());
			resp.setMsg(ReturnCode.SUCCESS.getMsg());
		} catch (Exception e) {
			logger.error("passengerFlowLevel.batchInsert() error{}", e);
			resp.setCode(ReturnCode.ERROR_01.getCode());
			resp.setMsg(ReturnCode.ERROR_01.getMsg());
		}
		return resp;
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public void delete(String passengerFlowLevelId) {
		try {
			if (StringUtils.isNotBlank(passengerFlowLevelId)) {
				passengerFlowLevelService.deleteByPrimaryKey(passengerFlowLevelId.split(","));
				// passengerFlowLevelService.deleteByPrimaryKey(passengerFlowLevelId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/planDelete")
	@ResponseBody
	public void planDelete(String menuId) {
		try {
			if (StringUtils.isNotBlank(menuId)) {
				passengerFlowLevelService.deletePlan(menuId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
