package com.lty.rt.passengerFlows.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StationPsgflow implements Serializable {
    /** UUID */
    private String id;

    /** 创建时间 */
    private Date createTime;

    /** 是否删除 0有效 1删除 */
    private String deleted;

    /** 站点ID */
    private String stationId;

    /** 站台ID */
    private String platformId;

    /** 设备编号 */
    private String onboardId;

    /** 事件触发时间 */
    private Date occurTime;

    /** 车厢内总人数 */
    private Integer totalPersonCount;

    /** 本站上车人数 */
    private Integer onbusPersonCount;

    /** 本站下车人数 */
    private Integer offbusPersonCount;

    /** 是否为假日  0:不是 1:是 */
    private BigDecimal holidayFlag;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted == null ? null : deleted.trim();
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId == null ? null : stationId.trim();
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId == null ? null : platformId.trim();
    }

    public String getOnboardId() {
        return onboardId;
    }

    public void setOnboardId(String onboardId) {
        this.onboardId = onboardId == null ? null : onboardId.trim();
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
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

    public BigDecimal getHolidayFlag() {
        return holidayFlag;
    }

    public void setHolidayFlag(BigDecimal holidayFlag) {
        this.holidayFlag = holidayFlag;
    }
}