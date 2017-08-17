package com.lty.rt.passengerFlows.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PlatFormPsgFlow implements Serializable {
	/** 线路ID */
	private String lineId;

	/** 线路名称 */
	private String lineName;

	/** 站台经度 */
	private String longitude;

	/** 站台纬度 */
	private String latitude;

	/** 站台名称 */
	private String platformName;

	/** 站台ID */
	private String platformId;

	/** 车厢内总人数 */
	private Integer totalPersonCount;

	/** 本站上车人数 */
	private Integer onbusPersonCount;

	/** 本站下车人数 */
	private Integer offbusPersonCount;

	List<Map<String, Object>> lines;

	private static final long serialVersionUID = 1L;

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public Integer getTotalPersonCount() {
		return totalPersonCount;
	}

	public void setTotalPersonCount(Integer totalPersonCount) {
		this.totalPersonCount = totalPersonCount;
	}

	public Integer getOnbusPersonCount() {
		return onbusPersonCount;
	}

	public void setOnbusPersonCount(Integer onbusPersonCount) {
		this.onbusPersonCount = onbusPersonCount;
	}

	public Integer getOffbusPersonCount() {
		return offbusPersonCount;
	}

	public void setOffbusPersonCount(Integer offbusPersonCount) {
		this.offbusPersonCount = offbusPersonCount;
	}

	public List<Map<String, Object>> getLines() {
		return lines;
	}

	public void setLines(List<Map<String, Object>> lines) {
		this.lines = lines;
	}

}