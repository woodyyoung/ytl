package com.lty.rt.assessment.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lty.rt.assessment.bean.IndexLevel;

@SuppressWarnings("serial")
public class IndexSourceDataVo implements Serializable {
	private String id;

    private String indexId;
    
    private String indexName;

    private BigDecimal indexNum;

    private BigDecimal indexTotalNum;

    private String countDate;

    private String createtime;

    private String updatetime;

    private String actualLevel;

    private BigDecimal actualScore;
    
    private String inputPerson;
    
    private String remark;
    
    //指标得分
  	private String  levelUnit;
	private IndexLevel actualIndexLevel;
	
	public String getLevelUnit() {
		return levelUnit;
	}

	public void setLevelUnit(String levelUnit) {
		this.levelUnit = levelUnit;
	}

	public IndexLevel getActualIndexLevel() {
		return actualIndexLevel;
	}

	public void setActualIndexLevel(IndexLevel actualIndexLevel) {
		this.actualIndexLevel = actualIndexLevel;
	}

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

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
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

	public String getCountDate() {
		return countDate;
	}

	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
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
    
}