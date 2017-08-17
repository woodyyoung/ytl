package com.lty.rt.basicData.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Line implements Serializable {
	/** UUID */
	private String id;

	/** 名称 */
	private String name;

	/** 线路编号 */
	private Integer gprsid;

	/** 是否禁用 0否 1是 */
	private Integer isDisable;

	/** 创建时间 */
	private Date createTime;

	/** 是否删除 0有效 1删除 */
	private Integer deleted;

	/** 简称 */
	private String shortname;

	/** 线路类型 */
	private BigDecimal linetype;

	/** 运营类型 */
	private BigDecimal runtype;

	/** 调度模式 */
	private BigDecimal dispatchmode;

	/** 线路方向 */
	private BigDecimal direction;

	/** 部门ID */
	private String departmentid;

	/** 区域类型 */
	private BigDecimal areatype;

	/** 线路车辆总数 */
	private BigDecimal totalbus;

	/** 售票类型 */
	private String tickettype;

	/** 票价 */
	private String ticketprice;

	/** 线路颜色 */
	private String dcolor;

	/** 站点形状 */
	private String dtext;

	/** 线条宽度 */
	private BigDecimal lwidth;

	/** 辅助线 */
	private Object lpath;

	/** 线路长度 */
	private double length;

	/** PID */
	private String pid;

	private static final long serialVersionUID = 1L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getGprsid() {
		return gprsid;
	}

	public void setGprsid(Integer gprsid) {
		this.gprsid = gprsid;
	}

	public Integer getIsDisable() {
		return isDisable;
	}

	public void setIsDisable(Integer isDisable) {
		this.isDisable = isDisable;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname == null ? null : shortname.trim();
	}

	public BigDecimal getLinetype() {
		return linetype;
	}

	public void setLinetype(BigDecimal linetype) {
		this.linetype = linetype;
	}

	public BigDecimal getRuntype() {
		return runtype;
	}

	public void setRuntype(BigDecimal runtype) {
		this.runtype = runtype;
	}

	public BigDecimal getDispatchmode() {
		return dispatchmode;
	}

	public void setDispatchmode(BigDecimal dispatchmode) {
		this.dispatchmode = dispatchmode;
	}

	public BigDecimal getDirection() {
		return direction;
	}

	public void setDirection(BigDecimal direction) {
		this.direction = direction;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid == null ? null : departmentid.trim();
	}

	public BigDecimal getAreatype() {
		return areatype;
	}

	public void setAreatype(BigDecimal areatype) {
		this.areatype = areatype;
	}

	public BigDecimal getTotalbus() {
		return totalbus;
	}

	public void setTotalbus(BigDecimal totalbus) {
		this.totalbus = totalbus;
	}

	public String getTickettype() {
		return tickettype;
	}

	public void setTickettype(String tickettype) {
		this.tickettype = tickettype == null ? null : tickettype.trim();
	}

	public String getTicketprice() {
		return ticketprice;
	}

	public void setTicketprice(String ticketprice) {
		this.ticketprice = ticketprice == null ? null : ticketprice.trim();
	}

	public String getDcolor() {
		return dcolor;
	}

	public void setDcolor(String dcolor) {
		this.dcolor = dcolor == null ? null : dcolor.trim();
	}

	public String getDtext() {
		return dtext;
	}

	public void setDtext(String dtext) {
		this.dtext = dtext == null ? null : dtext.trim();
	}

	public BigDecimal getLwidth() {
		return lwidth;
	}

	public void setLwidth(BigDecimal lwidth) {
		this.lwidth = lwidth;
	}

	public Object getLpath() {
		return lpath;
	}

	public void setLpath(Object lpath) {
		this.lpath = lpath;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}