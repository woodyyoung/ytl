package com.lty.rt.auxiliaryAnalysis.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 财政补贴类型
 * 
 * @author fc
 *
 */
public class FinancialType {
	
	private String financialtype_id;		//主键ID
	private String financialtype_name;		//补贴类型名称
	private String financialtype_object;	//补贴类型对象 1.公交财政补贴种类、2.公交财政补贴对象、3.公交财政补贴来源
	private String user_id;				//录入人
	private String user_name;			//录入人名称
	private Date create_time;			//录入时间
	private String note;				//备注
	
	public FinancialType(){}
	
	public FinancialType(String financialtype_id){ 
		this.financialtype_id = financialtype_id;
	}

	
	public String getFinancialtype_id() {
		return financialtype_id;
	}



	public void setFinancialtype_id(String financialtype_id) {
		this.financialtype_id = financialtype_id;
	}



	public String getFinancialtype_name() {
		return financialtype_name;
	}



	public void setFinancialtype_name(String financialtype_name) {
		this.financialtype_name = financialtype_name;
	}



	public String getUser_id() {
		return user_id;
	}
	

	public String getFinancialtype_object() {
		return financialtype_object;
	}



	public void setFinancialtype_object(String financialtype_object) {
		this.financialtype_object = financialtype_object;
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



	public String fieldIsEmpty(){
		if(StringUtils.isEmpty(financialtype_name))
			return "类型名称不能为空";
		return "";
	}
	
}
