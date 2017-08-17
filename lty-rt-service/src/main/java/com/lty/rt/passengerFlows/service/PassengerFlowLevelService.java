package com.lty.rt.passengerFlows.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lty.rt.basicData.treeUtil.TreeNode;
import com.lty.rt.comm.exception.ApplicationException;
import com.lty.rt.comm.exception.ReturnCode;
import com.lty.rt.comm.util.UUIDUtils;
import com.lty.rt.passengerFlows.bean.PassengerFlowLevel;
import com.lty.rt.passengerFlows.bean.PsgLevelPlan;
import com.lty.rt.passengerFlows.mapper.PassengerFlowLevelMapper;

@Transactional
@Service
public class PassengerFlowLevelService {
	@Autowired
	private PassengerFlowLevelMapper passengerFlowLevelMapper;

	public int deleteByPrimaryKey(String id) {
		return passengerFlowLevelMapper.deleteByPrimaryKey(id);
	}

	public int deleteByPrimaryKey(String[] ids) {
		return passengerFlowLevelMapper.deleteByPrimaryKey(ids);
	}

	public int insert(PassengerFlowLevel record) {
		return passengerFlowLevelMapper.insert(record);
	}

	public PassengerFlowLevel selectByPrimaryKey(String id) {
		return passengerFlowLevelMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKey(PassengerFlowLevel record) {
		return passengerFlowLevelMapper.updateByPrimaryKey(record);
	}

	public List<Map<String, Object>> findListByMap(Map<String, Object> map) {
		return passengerFlowLevelMapper.findListByMap(map);
	}

	public List<TreeNode> findTreeListByMap(Map<String, Object> map) {
		return passengerFlowLevelMapper.findTreeListByMap(map);
	}

	public PsgLevelPlan save(PsgLevelPlan psgLevelPlan) {
		if (psgLevelPlan == null) {
			throw new ApplicationException(ReturnCode.ERROR_02.getCode(),
					ReturnCode.ERROR_02.getMsg() + ":psgLevelPlan");
		}
		String planName = psgLevelPlan.getPlanName();
		String menuId = psgLevelPlan.getMenuId();
		if (StringUtils.isNotBlank(psgLevelPlan.getPlanName())) {
			Map<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("planName", planName);
			conditionMap.put("menuId", menuId);
			if (StringUtils.isNotBlank(passengerFlowLevelMapper.getPlanByName(conditionMap))) {
				throw new ApplicationException(ReturnCode.ERROR_02.getCode(), ReturnCode.ERROR_02.getMsg() + ":方案名重复");
			}

		} else {
			throw new ApplicationException(ReturnCode.ERROR_02.getCode(), ReturnCode.ERROR_02.getMsg() + ":planName");
		}
		if (StringUtils.isBlank(psgLevelPlan.getId())) {// 新增
			String dataType = psgLevelPlan.getDataType();
			String dataTypeIdStr = "";
			if (StringUtils.isNotBlank(menuId)) {// add一级菜单
				dataTypeIdStr = passengerFlowLevelMapper.getMaxdataTypeId(menuId);
				if (StringUtils.isBlank(dataTypeIdStr)) {
					dataTypeIdStr = dataType + "001";
				}
			}
			psgLevelPlan.setId(UUIDUtils.getUUID().toString());
			Integer newDataTypeId = Integer.valueOf(dataTypeIdStr);
			psgLevelPlan.setDataTypeId(String.format("%0" + dataTypeIdStr.length() + "d", newDataTypeId + 1)); // 生成自已的code
			passengerFlowLevelMapper.insertPsgLevelPlan(psgLevelPlan);
		}
		return psgLevelPlan;
	}

	public int batchSave(Map<String, Object> map) {
		int count = 0;
		String menuId = map.get("menuId") == null ? "" : map.get("menuId").toString();
		String planName = map.get("planName") == null ? "" : map.get("planName").toString();
		String maxData = map.get("maxData") == null ? "" : map.get("maxData").toString();
		String dataType = map.get("dataType") == null ? "" : map.get("dataType").toString();
		PsgLevelPlan psgLevelPlan = new PsgLevelPlan();
		psgLevelPlan.setDataType(dataType);
		psgLevelPlan.setPlanName(planName);
		psgLevelPlan.setMenuId(menuId);
		psgLevelPlan = save(psgLevelPlan);
		dataType = psgLevelPlan.getDataTypeId() == null ? "" : psgLevelPlan.getDataTypeId();
		String colors = map.get("colors") == null ? "" : map.get("colors").toString().replace("[", "").replace("]", "");
		String[] cols = colors.split(";,");
		List<PassengerFlowLevel> list = new ArrayList<PassengerFlowLevel>();
		if (StringUtils.isNotBlank(dataType) && StringUtils.isNotBlank(maxData) && StringUtils.isNotBlank(colors)) {
			BigDecimal[] datas = getAvgDatas(maxData);
			for (int i = 0; i < 10; i++) {
				PassengerFlowLevel pfl = new PassengerFlowLevel();
				pfl.setId(UUIDUtils.getUUID().toString());
				pfl.setMindata(datas[i]);
				pfl.setMaxdata(datas[i + 1]);
				pfl.setCirclecolor(cols[i].replaceAll(";", ""));
				pfl.setIsdisable(new BigDecimal(0));
				pfl.setLevelname((i + 1) + "级");
				pfl.setCirclesize(new BigDecimal(10 * (i + 1)));
				pfl.setDeleted("");
				pfl.setRemark("");
				pfl.setDataType(dataType);
				pfl.setPlanid("");
				list.add(pfl);
				passengerFlowLevelMapper.insert(pfl);
			}
		}

		/*
		 * if (CollectionUtils.isNotEmpty(list)) { count =
		 * passengerFlowLevelMapper.batchInsert(list); }
		 */

		return count;
	}

	private static BigDecimal[] getAvgDatas(String maxData) {
		BigDecimal[] retDatas = new BigDecimal[11];
		retDatas[0] = new BigDecimal(0);
		Integer maxDataValue = Integer.valueOf(maxData);
		Integer basicData = maxDataValue / 10;
		for (int i = 1; i < retDatas.length - 1; i++) {
			retDatas[i] = new BigDecimal(basicData * i);
		}
		retDatas[10] = new BigDecimal(maxDataValue * 10000);

		return retDatas;
	}

	@Transactional
	public int deletePlan(String menuId) {
		passengerFlowLevelMapper.deleteByDataTypeId(menuId);
		return passengerFlowLevelMapper.deletePlan(menuId);

	}

	public void close(Map<String, Object> map) {
		// String menuId = map.get("menuId") == null ? "" :
		// map.get("menuId").toString();
		String dataType = map.get("dataType") == null ? "" : map.get("dataType").toString();
		if (StringUtils.isNotBlank(dataType)) {
			passengerFlowLevelMapper.closePlan(map);
		}
	}

	public List<PsgLevelPlan> getpsgPlanAndLevelByMap(Map<String, Object> map) {
		return passengerFlowLevelMapper.getpsgPlanAndLevelByMap(map);

	}

}