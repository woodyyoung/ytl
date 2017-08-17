package com.lty.rt.passengerFlows.bean;

import java.math.BigDecimal;
import java.util.Date;

public class IndustryCost {
	private String id;

	private String occurtime;

	private String costTypeNo;

	private BigDecimal cost;

	private Date createtime;

	private String createby;

	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getOccurtime() {
		return occurtime;
	}

	public void setOccurtime(String occurtime) {
		this.occurtime = occurtime;
	}

	public String getCostTypeNo() {
		return costTypeNo;
	}

	public void setCostTypeNo(String costTypeNo) {
		this.costTypeNo = costTypeNo == null ? null : costTypeNo.trim();
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby == null ? null : createby.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}
}