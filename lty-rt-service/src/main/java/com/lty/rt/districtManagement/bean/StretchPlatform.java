package com.lty.rt.districtManagement.bean;

public class StretchPlatform {
    private String stretch;

    private String platformid;

    private String stretchlineid;

    public String getStretch() {
        return stretch;
    }

    public void setStretch(String stretch) {
        this.stretch = stretch == null ? null : stretch.trim();
    }

    public String getPlatformid() {
        return platformid;
    }

    public void setPlatformid(String platformid) {
        this.platformid = platformid == null ? null : platformid.trim();
    }

    public String getStretchlineid() {
        return stretchlineid;
    }

    public void setStretchlineid(String stretchlineid) {
        this.stretchlineid = stretchlineid == null ? null : stretchlineid.trim();
    }
}