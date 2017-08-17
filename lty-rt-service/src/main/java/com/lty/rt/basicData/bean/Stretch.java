package com.lty.rt.basicData.bean;

import java.math.BigDecimal;

public class Stretch {
    private String id;

    private String linename;

    private String lineid;

    private String parentlineid;

    private BigDecimal linelength;

    private String track;

    private Integer state;

    private String remark;
    
    private Integer levels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getLinename() {
        return linename;
    }

    public void setLinename(String linename) {
        this.linename = linename == null ? null : linename.trim();
    }

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid == null ? null : lineid.trim();
    }

    public String getParentlineid() {
        return parentlineid;
    }

    public void setParentlineid(String parentlineid) {
        this.parentlineid = parentlineid == null ? null : parentlineid.trim();
    }

    public BigDecimal getLinelength() {
        return linelength;
    }

    public void setLinelength(BigDecimal linelength) {
        this.linelength = linelength;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track == null ? null : track.trim();
    }

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getLevels() {
		return levels;
	}

	public void setLevels(Integer levels) {
		this.levels = levels;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}