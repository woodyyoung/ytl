package com.lty.rt.auxiliaryAnalysis.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 公交行业人车成本实体类
 * 
 * @author fc
 *
 */
public class PVCost {
	private String pvcost_id;   		//UUID主键
	private String year;				//发生年份
	private String money;				//成本类型涉及金额
	private String user_id;				//录入人
	private String user_name;			//录入人名称
	private String line_id;	
	private String company_id;
	private Date create_time;			//录入时间
	private String note;				//备注
	
	private String startTime;
	private String endTime;
	
	private String pvcosttype_id="";		//主键ID
	private String pvcosttype_name="";		//成本类型名称
	private String pvcosttype_user_id="";				//录入人
	private String pvcosttype_user_name="";			//录入人名称
	private Date pvcosttype_create_time=new Date();			//录入时间
	private String pvcosttype_note="";				//备注
	
	
	
	public String getPvcost_id() {
		return pvcost_id;
	}



	public String getStartTime() {
		return startTime;
	}



	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}



	public String getEndTime() {
		return endTime;
	}



	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}



	public void setPvcost_id(String pvcost_id) {
		this.pvcost_id = pvcost_id;
	}



	public String getYear() {
		return year;
	}



	public void setYear(String year) {
		this.year = year;
	}



	public String getMoney() {
		return money;
	}



	public void setMoney(String money) {
		this.money = money;
	}



	public String getUser_id() {
		return user_id;
	}



	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}



	public String getUser_name() {
		return user_name;
	}



	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}



	public String getLine_id() {
		return line_id;
	}



	public void setLine_id(String line_id) {
		this.line_id = line_id;
	}



	public String getCompany_id() {
		return company_id;
	}



	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}



	public Date getCreate_time() {
		return create_time;
	}



	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}



	public String getNote() {
		return note;
	}



	public void setNote(String note) {
		this.note = note;
	}



	public String getPvcosttype_id() {
		return pvcosttype_id;
	}



	public void setPvcosttype_id(String pvcosttype_id) {
		this.pvcosttype_id = pvcosttype_id;
	}



	public String getPvcosttype_name() {
		return pvcosttype_name;
	}



	public void setPvcosttype_name(String pvcosttype_name) {
		this.pvcosttype_name = pvcosttype_name;
	}



	public String getPvcosttype_user_id() {
		return pvcosttype_user_id;
	}



	public void setPvcosttype_user_id(String pvcosttype_user_id) {
		this.pvcosttype_user_id = pvcosttype_user_id;
	}



	public String getPvcosttype_user_name() {
		return pvcosttype_user_name;
	}



	public void setPvcosttype_user_name(String pvcosttype_user_name) {
		this.pvcosttype_user_name = pvcosttype_user_name;
	}



	public Date getPvcosttype_create_time() {
		return pvcosttype_create_time;
	}



	public void setPvcosttype_create_time(Date pvcosttype_create_time) {
		this.pvcosttype_create_time = pvcosttype_create_time;
	}



	public String getPvcosttype_note() {
		return pvcosttype_note;
	}



	public void setPvcosttype_note(String pvcosttype_note) {
		this.pvcosttype_note = pvcosttype_note;
	}



	public String fieldIsEmpty(){
		if(StringUtils.isEmpty(year)){
			return "录入月份不能为空";
		}else if(StringUtils.isEmpty(line_id)){
			return "请选择线路";
		}else if(StringUtils.isEmpty(company_id)){
			return "请选择公司";
		}else if(StringUtils.isEmpty(money)){
			return "请输入金额";
		}
		return "";
	}
}


