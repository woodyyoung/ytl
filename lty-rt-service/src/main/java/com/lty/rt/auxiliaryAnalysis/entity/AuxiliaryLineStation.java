package com.lty.rt.auxiliaryAnalysis.entity;

/**
 * 辅助线路方案站点---保存的是方案-线路-站台之间关系
 * @author Administrator
 *
 */
public class AuxiliaryLineStation {
	private String lineId;
	
	private String schemeId;
	
	private String platformId;

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
}
