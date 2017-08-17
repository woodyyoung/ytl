package com.lty.rt.districtManagement.bean;

public class StretchMap {
    private String stretchid;

    private Double lat;

    private Double ing;

    private Integer orderby;

    public String getStretchid() {
        return stretchid;
    }

    public void setStretchid(String stretchid) {
        this.stretchid = stretchid == null ? null : stretchid.trim();
    }

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getIng() {
		return ing;
	}

	public void setIng(Double ing) {
		this.ing = ing;
	}

	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

}