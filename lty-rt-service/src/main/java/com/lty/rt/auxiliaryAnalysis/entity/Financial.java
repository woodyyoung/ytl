package com.lty.rt.auxiliaryAnalysis.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 财政统计
 * 
 * @author fc
 *
 */
public class Financial {
	private String financial_id;   		//UUID主键
	private String year;				//补贴时间
	private String money;				//补贴额度
	private String user_id;				//录入人
	private String user_name;			//录入人名称
	private Date create_time;			//录入时间
	private String note;				//备注
	
	private String startTime;
	private String endTime;
	
	private String	 financialtype_type_id              ="" ;
	private String   financialtype_type_name            ="" ;
	private String   financialtype_type_user_id         ="" ;
	private String   financialtype_type_user_name       ="" ;
	private String   financialtype_type_object          ="" ;
	private Date   financialtype_type_create_time     =new Date() ;
	private String   financialtype_type_note            ="" ;
	       
	private String   financialtype_source_id          ="" ;
	private String   financialtype_source_name        ="" ;
	private String   financialtype_source_user_id     ="" ;
	private String   financialtype_source_user_name   ="" ;
	private String   financialtype_source_object      ="" ;
	private Date   financialtype_source_create_time =new Date() ;
	private String   financialtype_source_note        ="" ;
	                         
	private String   financialtype_object_id          ="" ;
	private String   financialtype_object_name        ="" ;
	private String   financialtype_object_user_id     ="" ;
	private String   financialtype_object_user_name   ="" ;
	private String   financialtype_object_object      ="" ;
	private Date   financialtype_object_create_time =new Date() ;
	private String   financialtype_object_note        ="" ;
	

	

	public String getFinancial_id() {
		return financial_id;
	}




	public void setFinancial_id(String financial_id) {
		this.financial_id = financial_id;
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




	public String getFinancialtype_type_id() {
		return financialtype_type_id;
	}




	public void setFinancialtype_type_id(String financialtype_type_id) {
		this.financialtype_type_id = financialtype_type_id;
	}




	public String getFinancialtype_type_name() {
		return financialtype_type_name;
	}




	public void setFinancialtype_type_name(String financialtype_type_name) {
		this.financialtype_type_name = financialtype_type_name;
	}




	public String getFinancialtype_type_user_id() {
		return financialtype_type_user_id;
	}




	public void setFinancialtype_type_user_id(String financialtype_type_user_id) {
		this.financialtype_type_user_id = financialtype_type_user_id;
	}




	public String getFinancialtype_type_user_name() {
		return financialtype_type_user_name;
	}




	public void setFinancialtype_type_user_name(String financialtype_type_user_name) {
		this.financialtype_type_user_name = financialtype_type_user_name;
	}




	public String getFinancialtype_type_object() {
		return financialtype_type_object;
	}




	public void setFinancialtype_type_object(String financialtype_type_object) {
		this.financialtype_type_object = financialtype_type_object;
	}




	public Date getFinancialtype_type_create_time() {
		return financialtype_type_create_time;
	}




	public void setFinancialtype_type_create_time(Date financialtype_type_create_time) {
		this.financialtype_type_create_time = financialtype_type_create_time;
	}




	public String getFinancialtype_type_note() {
		return financialtype_type_note;
	}




	public void setFinancialtype_type_note(String financialtype_type_note) {
		this.financialtype_type_note = financialtype_type_note;
	}




	public String getFinancialtype_source_id() {
		return financialtype_source_id;
	}




	public void setFinancialtype_source_id(String financialtype_source_id) {
		this.financialtype_source_id = financialtype_source_id;
	}




	public String getFinancialtype_source_name() {
		return financialtype_source_name;
	}




	public void setFinancialtype_source_name(String financialtype_source_name) {
		this.financialtype_source_name = financialtype_source_name;
	}




	public String getFinancialtype_source_user_id() {
		return financialtype_source_user_id;
	}




	public void setFinancialtype_source_user_id(String financialtype_source_user_id) {
		this.financialtype_source_user_id = financialtype_source_user_id;
	}




	public String getFinancialtype_source_user_name() {
		return financialtype_source_user_name;
	}




	public void setFinancialtype_source_user_name(String financialtype_source_user_name) {
		this.financialtype_source_user_name = financialtype_source_user_name;
	}




	public String getFinancialtype_source_object() {
		return financialtype_source_object;
	}




	public void setFinancialtype_source_object(String financialtype_source_object) {
		this.financialtype_source_object = financialtype_source_object;
	}




	public Date getFinancialtype_source_create_time() {
		return financialtype_source_create_time;
	}




	public void setFinancialtype_source_create_time(Date financialtype_source_create_time) {
		this.financialtype_source_create_time = financialtype_source_create_time;
	}




	public String getFinancialtype_source_note() {
		return financialtype_source_note;
	}




	public void setFinancialtype_source_note(String financialtype_source_note) {
		this.financialtype_source_note = financialtype_source_note;
	}




	public String getFinancialtype_object_id() {
		return financialtype_object_id;
	}




	public void setFinancialtype_object_id(String financialtype_object_id) {
		this.financialtype_object_id = financialtype_object_id;
	}




	public String getFinancialtype_object_name() {
		return financialtype_object_name;
	}




	public void setFinancialtype_object_name(String financialtype_object_name) {
		this.financialtype_object_name = financialtype_object_name;
	}




	public String getFinancialtype_object_user_id() {
		return financialtype_object_user_id;
	}




	public void setFinancialtype_object_user_id(String financialtype_object_user_id) {
		this.financialtype_object_user_id = financialtype_object_user_id;
	}




	public String getFinancialtype_object_user_name() {
		return financialtype_object_user_name;
	}




	public void setFinancialtype_object_user_name(String financialtype_object_user_name) {
		this.financialtype_object_user_name = financialtype_object_user_name;
	}




	public String getFinancialtype_object_object() {
		return financialtype_object_object;
	}




	public void setFinancialtype_object_object(String financialtype_object_object) {
		this.financialtype_object_object = financialtype_object_object;
	}




	public Date getFinancialtype_object_create_time() {
		return financialtype_object_create_time;
	}




	public void setFinancialtype_object_create_time(Date financialtype_object_create_time) {
		this.financialtype_object_create_time = financialtype_object_create_time;
	}




	public String getFinancialtype_object_note() {
		return financialtype_object_note;
	}




	public void setFinancialtype_object_note(String financialtype_object_note) {
		this.financialtype_object_note = financialtype_object_note;
	}




	public String fieldIsEmpty(){
		if(StringUtils.isEmpty(year)){
			return "录入月份不能为空";
		}else if(StringUtils.isEmpty(money)){
			return "请输入金额";
		}
		return "";
	}
}
