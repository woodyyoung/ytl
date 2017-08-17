package com.lty.rt.basicData.bean;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class PjmkArea implements Serializable{
	
    private String id;

    private String codename;

    private String codeid;

    private String parentcodeid;

    private Integer propertiesid;

    private String properties;

    private BigDecimal area;

    private String remark;
    
    private Integer levels;
    
    private Integer state = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename == null ? null : codename.trim();
    }

    public String getCodeid() {
        return codeid;
    }

    public void setCodeid(String codeid) {
        this.codeid = codeid == null ? null : codeid.trim();
    }

    public String getParentcodeid() {
        return parentcodeid;
    }

    public void setParentcodeid(String parentcodeid) {
        this.parentcodeid = parentcodeid == null ? null : parentcodeid.trim();
    }

    public Integer getPropertiesid() {
        return propertiesid;
    }

    public void setPropertiesid(Integer propertiesid) {
        this.propertiesid = propertiesid;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties == null ? null : properties.trim();
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public Integer getLevels() {
		return levels;
	}

	public void setLevels(Integer levels) {
		this.levels = levels;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}