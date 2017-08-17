package com.lty.rt.districtManagement.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AreaPlatform implements Serializable{
    private String areaId;

    private String platFormId;
    
    private String areaCodeId;

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getPlatFormId() {
		return platFormId;
	}

	public void setPlatFormId(String platFormId) {
		this.platFormId = platFormId;
	}

	public String getAreaCodeId() {
		return areaCodeId;
	}

	public void setAreaCodeId(String areaCodeId) {
		this.areaCodeId = areaCodeId;
	}

}