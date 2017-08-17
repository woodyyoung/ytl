package com.lty.rt.districtManagement.bean;

public class AreaMap {
    private String areaid;

    private Double lat;

    private Double ing;

    private Integer orderby;
    
    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid == null ? null : areaid.trim();
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