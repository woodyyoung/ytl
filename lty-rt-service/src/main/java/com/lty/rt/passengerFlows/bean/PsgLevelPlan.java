package com.lty.rt.passengerFlows.bean;

import java.io.Serializable;
import java.util.List;

public class PsgLevelPlan implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** UUID */
	private String id;

	/** 方案名称 */
	private String planName;

	private String dataType;

	private String dataTypeId;

	private String menuId;
	private String remark;
	List<PassengerFlowLevel> levelList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataTypeId() {
		return dataTypeId;
	}

	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PassengerFlowLevel> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<PassengerFlowLevel> levelList) {
		this.levelList = levelList;
	}

}