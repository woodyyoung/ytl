package com.lty.rt.passengerFlows.bean;

import java.math.BigDecimal;
import java.util.Date;

public class PassengerFlowLevel {
	private String id;

	private Date createtime;

	private String deleted;

	private String levelname;

	private String circlecolor;

	private BigDecimal circlesize;

	private BigDecimal maxdata;

	private BigDecimal mindata;

	private String remark;

	private String dataType;

	private String planid;

	private BigDecimal isdisable;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted == null ? null : deleted.trim();
	}

	public String getLevelname() {
		return levelname;
	}

	public void setLevelname(String levelname) {
		this.levelname = levelname == null ? null : levelname.trim();
	}

	public String getCirclecolor() {
		return circlecolor;
	}

	public void setCirclecolor(String circlecolor) {
		this.circlecolor = circlecolor == null ? null : circlecolor.trim();
	}

	public BigDecimal getCirclesize() {
		return circlesize;
	}

	public void setCirclesize(BigDecimal circlesize) {
		this.circlesize = circlesize;
	}

	public BigDecimal getMaxdata() {
		return maxdata;
	}

	public void setMaxdata(BigDecimal maxdata) {
		this.maxdata = maxdata;
	}

	public BigDecimal getMindata() {
		return mindata;
	}

	public void setMindata(BigDecimal mindata) {
		this.mindata = mindata;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public BigDecimal getIsdisable() {
		return isdisable;
	}

	public void setIsdisable(BigDecimal isdisable) {
		this.isdisable = isdisable;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getPlanid() {
		return planid;
	}

	public void setPlanid(String planid) {
		this.planid = planid;
	}

}