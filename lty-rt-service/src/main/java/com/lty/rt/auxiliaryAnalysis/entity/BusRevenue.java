package com.lty.rt.auxiliaryAnalysis.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BusRevenue implements Serializable {
    /** 主键ID */
    private String id;

    /** 营收 */
    private Long revenue;

    /** 发生年份 */
    private Date occurrenceyear;

    /** 录入时间 */
    private Date entrytime;

    /** 录入人 */
    private String inputman;

    /** 备注 */
    private String remarks;
    
    private String yearStr;

    private static final long serialVersionUID = 1L;
    
    private SimpleDateFormat sd = new SimpleDateFormat("yyyy");

    public void convertToYear() throws ParseException{
    	if(StringUtils.isNotBlank(yearStr)){
    		setOccurrenceyear(sd.parse(yearStr));
    	}
    }
    
    public String getYearStr() {
		return yearStr;
	}

	public void setYearStr(String yearStr) {
		this.yearStr = yearStr;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public Date getOccurrenceyear() {
        return occurrenceyear;
    }

    public void setOccurrenceyear(Date occurrenceyear) {
        this.occurrenceyear = occurrenceyear;
        if(this.occurrenceyear!=null){
        	this.yearStr = sd.format(occurrenceyear);
        }
    }

    public Date getEntrytime() {
        return entrytime;
    }

    public void setEntrytime(Date entrytime) {
        this.entrytime = entrytime;
    }

    public String getInputman() {
        return inputman;
    }

    public void setInputman(String inputman) {
        this.inputman = inputman == null ? null : inputman.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}