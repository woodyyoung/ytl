package com.lty.rt.assessment.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lty.rt.assessment.bean.IndexLevel;

@SuppressWarnings("serial")
public class IndexTotalDataVo implements Serializable {
	/**
	 * 指标名称
	 */
	private String indexId;
	
	/**
	 * 指标名称
	 */
	private String name;
	
	/**
	 * 权重
	 */
	private BigDecimal weight;
	
	
	/**
	 * 目标
	 */
	private BigDecimal targetLevel;
	
	/**
	 * 目标等级
	 */
	private IndexLevel targetIndexLevel;
	
	/**
	 * 日期
	 */
	private String countDate;
	
	/**
	 * 得分
	 */
	private BigDecimal actualLevel;
	
	private IndexLevel actualIndexLevel;
	
	/**
	 * 说明
	 */
	private String description;
	
	/**
	 * 备用字段
	 */
	private String arg;
	
	//指标得分
	private BigDecimal actualScore;
	//指标得分
	private String  levelUnit;
	
	

	public String getLevelUnit() {
		return levelUnit;
	}

	public void setLevelUnit(String levelUnit) {
		this.levelUnit = levelUnit;
	}

	public BigDecimal getActualScore() {
		return actualScore;
	}

	public void setActualScore(BigDecimal actualScore) {
		this.actualScore = actualScore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getTargetLevel() {
		return targetLevel;
	}

	public void setTargetLevel(BigDecimal targetLevel) {
		this.targetLevel = targetLevel;
	}

	public String getCountDate() {
		return countDate;
	}

	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}

	public BigDecimal getActualLevel() {
		return actualLevel;
	}

	public void setActualLevel(BigDecimal actualLevel) {
		this.actualLevel = actualLevel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getArg() {
		return arg;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}

	public IndexLevel getTargetIndexLevel() {
		return targetIndexLevel;
	}

	public void setTargetIndexLevel(IndexLevel targetIndexLevel) {
		this.targetIndexLevel = targetIndexLevel;
	}

	public IndexLevel getActualIndexLevel() {
		return actualIndexLevel;
	}

	public void setActualIndexLevel(IndexLevel actualIndexLevel) {
		this.actualIndexLevel = actualIndexLevel;
	}

	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	
	

}