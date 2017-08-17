package com.lty.rt.assessment.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class IndexLevel implements Serializable{
	
    private String indexId;

    private String indexLevel;

    private BigDecimal topLimit;

    private BigDecimal lowerLimit;

    private String description;

    private Date createtime;

    private Date updatetime;
    
    private String proposal;

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId == null ? null : indexId.trim();
    }

    public String getIndexLevel() {
        return indexLevel;
    }

    public void setIndexLevel(String indexLevel) {
        this.indexLevel = indexLevel == null ? null : indexLevel.trim();
    }


    public BigDecimal getTopLimit() {
		return topLimit;
	}

	public void setTopLimit(BigDecimal topLimit) {
		this.topLimit = topLimit;
	}

	public BigDecimal getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(BigDecimal lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	public String getProposal() {
		return proposal;
	}

	public void setProposal(String proposal) {
		this.proposal = proposal;
	}
}