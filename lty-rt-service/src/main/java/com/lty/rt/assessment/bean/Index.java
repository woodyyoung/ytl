package com.lty.rt.assessment.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Index implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String pkid;

    private String id;

    private String name;

    private String descriptions;

    private String parentid;

    private BigDecimal weight;
    
    private BigDecimal syntheticWeight;
    
    private Integer indexType;

    private String levelUnit;

    private String targetCountUnit;

    private String totalCountUnit;

    private BigDecimal isAble;

    private Date createtime;

    private Date updatetime;
    
    private String targetlevel;
    
    private List<IndexLevel> indexLevelList;

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getLevelUnit() {
		return levelUnit;
	}

	public void setLevelUnit(String levelUnit) {
		this.levelUnit = levelUnit;
	}

	public String getTargetCountUnit() {
		return targetCountUnit;
	}

	public void setTargetCountUnit(String targetCountUnit) {
		this.targetCountUnit = targetCountUnit;
	}

	public String getTotalCountUnit() {
		return totalCountUnit;
	}

	public void setTotalCountUnit(String totalCountUnit) {
		this.totalCountUnit = totalCountUnit;
	}

	public BigDecimal getIsAble() {
		return isAble;
	}

	public void setIsAble(BigDecimal isAble) {
		this.isAble = isAble;
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

	public List<IndexLevel> getIndexLevelList() {
		return indexLevelList;
	}

	public void setIndexLevelList(List<IndexLevel> indexLevelList) {
		this.indexLevelList = indexLevelList;
	}

	public String getTargetlevel() {
		return targetlevel;
	}

	public void setTargetlevel(String targetlevel) {
		this.targetlevel = targetlevel;
	}

	public BigDecimal getSyntheticWeight() {
		return syntheticWeight;
	}

	public void setSyntheticWeight(BigDecimal syntheticWeight) {
		this.syntheticWeight = syntheticWeight;
	}

	public Integer getIndexType() {
		return indexType;
	}

	public void setIndexType(Integer indexType) {
		this.indexType = indexType;
	}
    
}