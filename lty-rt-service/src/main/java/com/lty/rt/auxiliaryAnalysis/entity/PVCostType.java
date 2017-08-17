package com.lty.rt.auxiliaryAnalysis.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 公交行业人车成本类型实体类
 * 
 * @author fc
 *
 */
public class PVCostType {
	private String pvcosttype_id;		//主键ID
	private String pvcosttype_name;		//成本类型名称
	private String user_id;				//录入人
	private String user_name;			//录入人名称
	private Date create_time;			//录入时间
	private String note;				//备注
	
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
	
	public String fieldIsEmpty(){
		if(StringUtils.isEmpty(pvcosttype_name))
			return "类型名称不能为空";
		return "";
	}
	
}
