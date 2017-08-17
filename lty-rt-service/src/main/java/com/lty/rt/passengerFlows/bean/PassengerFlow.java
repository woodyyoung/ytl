package com.lty.rt.passengerFlows.bean;

import java.io.Serializable;

public class PassengerFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String stationId;

	private String stationName;

	private String longitude;

	private String latitude;

	private String totalpersoncount;

	private String onbuspersoncount;

	private String offbuspersoncount;

	private String startTime;

	private String endTime;

	private String occurTime;

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId == null ? null : stationId.trim();
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
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

	public String getTotalpersoncount() {
		return totalpersoncount;
	}

	public void setTotalpersoncount(String totalpersoncount) {
		this.totalpersoncount = totalpersoncount;
	}

	public String getOnbuspersoncount() {
		return onbuspersoncount;
	}

	public void setOnbuspersoncount(String onbuspersoncount) {
		this.onbuspersoncount = onbuspersoncount;
	}

	public String getOffbuspersoncount() {
		return offbuspersoncount;
	}

	public void setOffbuspersoncount(String offbuspersoncount) {
		this.offbuspersoncount = offbuspersoncount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(String occurTime) {
		this.occurTime = occurTime;
	}

}