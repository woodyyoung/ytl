package com.lty.rt.assessment.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class IndexSourceData implements Serializable {
	private String id;

    private String indexId;

    private BigDecimal indexNum;

    private BigDecimal indexTotalNum;

    private Date countDate;

    private Date createtime;

    private Date updatetime;

    private String actualLevel;

    private BigDecimal actualScore;
    
    private String lineId;
    
    private String areaId;
    
    private int isWork;
    
    private String inputPerson;
    private String remark;
    

	public String getInputPerson() {
		return inputPerson;
	}

	public void setInputPerson(String inputPerson) {
		this.inputPerson = inputPerson;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	public BigDecimal getIndexNum() {
		return indexNum;
	}

	public void setIndexNum(BigDecimal indexNum) {
		this.indexNum = indexNum;
	}

	public BigDecimal getIndexTotalNum() {
		return indexTotalNum;
	}

	public void setIndexTotalNum(BigDecimal indexTotalNum) {
		this.indexTotalNum = indexTotalNum;
	}

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
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

	public String getActualLevel() {
		return actualLevel;
	}

	public void setActualLevel(String actualLevel) {
		this.actualLevel = actualLevel;
	}

	public BigDecimal getActualScore() {
		return actualScore;
	}

	public void setActualScore(BigDecimal actualScore) {
		this.actualScore = actualScore;
	}

	public int getIsWork() {
		return isWork;
	}

	public void setIsWork(int isWork) {
		this.isWork = isWork;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
    
}