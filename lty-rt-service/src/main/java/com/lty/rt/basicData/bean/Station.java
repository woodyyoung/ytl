package com.lty.rt.basicData.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Station implements Serializable {
    /** UUID */
    private String id;

    /** 创建时间 */
    private Date createTime;

    /** 是否删除 0有效 1删除 */
    private Integer deleted;

    /** 名称 */
    private String name;

    /** 所属线路ID */
    private String lineid;

    /** 线路编号 */
    private Integer gprsid;

    /** 站序 */
    private Integer orderno;

    /** 方向 0上行 1下行 */
    private Integer direction;

    /** 与上站距离 */
    private BigDecimal bystartdIstance;

    /** 经度 */
    private BigDecimal longitude;

    /** 纬度 */
    private BigDecimal latitude;

    /** 方位角 */
    private BigDecimal angle;

    /** 是否禁用 0否 1是 */
    private Integer isDisable;

    /** 站台ID */
    private String platformId;

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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid == null ? null : lineid.trim();
    }

    public Integer getGprsid() {
        return gprsid;
    }

    public void setGprsid(Integer gprsid) {
        this.gprsid = gprsid;
    }

    public Integer getOrderno() {
        return orderno;
    }

    public void setOrderno(Integer orderno) {
        this.orderno = orderno;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public BigDecimal getBystartdIstance() {
        return bystartdIstance;
    }

    public void setBystartdIstance(BigDecimal bystartdIstance) {
        this.bystartdIstance = bystartdIstance;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getAngle() {
        return angle;
    }

    public void setAngle(BigDecimal angle) {
        this.angle = angle;
    }

    public Integer getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId == null ? null : platformId.trim();
    }
}